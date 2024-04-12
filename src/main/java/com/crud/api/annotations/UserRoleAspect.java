package com.crud.api.annotations;

import com.crud.api.exception.UnAuthenticatedException;
import com.crud.api.type.UserRoleType;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.crud.api.type.UserRoleType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class UserRoleAspect {

    @Pointcut("@annotation(HasRole)")
    public void hasRoleAnnotation() {
    }

    @Before("hasRoleAnnotation() && @annotation(hasRole)")
    public void checkUserRole(HasRole hasRole) throws Exception {
        List<UserRoleType> requiredRoles = new ArrayList<>(Arrays.stream(hasRole.value()).toList()); // Get the required role from the annotation
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnAuthenticatedException("User is not authenticated");
        }
        List<String> userRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        boolean hasAuthority = requiredRoles.stream()
                .map(UserRoleType::toString)
                .map(String::trim)
                .anyMatch(userRoles::contains);
        if (!hasAuthority) {
            throw new UnAuthenticatedException("User does not have required role");
        }
    }
}