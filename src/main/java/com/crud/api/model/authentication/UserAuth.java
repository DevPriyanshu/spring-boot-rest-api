package com.crud.api.model.authentication;


import com.crud.api.config.AppUserDetails;
import com.crud.api.model.Role;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class UserAuth implements Authentication {
    private final User user;
    private final Set<SimpleGrantedAuthority> authorities = new HashSet<>();

    public UserAuth(com.crud.api.model.User user) {
        this(user.getId(), user.getUsername(), user.getPassword(), user.getRoles());
    }

    public UserAuth(Long userId, String username, String password, Set<Role> roles) {
        roles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRoleType().toString()));
        });
        this.user = new AppUserDetails(userId, username, password, authorities);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.user;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    }

    @Override
    public String getName() {
        return this.user.getUsername();
    }
}
