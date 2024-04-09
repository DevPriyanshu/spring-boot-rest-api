package com.crud.api;

import com.crud.api.model.Role;
import com.crud.api.model.User;
import com.crud.api.repository.jpa.UserRepository;
import com.crud.api.type.UserRoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class InitSuperAdmin implements ApplicationRunner {
    @Autowired
    private UserRepository userRepository;


    @Override
    public void run(ApplicationArguments args) {
        initializeUsers();
    }

    private void initializeUsers() {
        if (userRepository.count() == 0) {
            Role role = Role.builder()
                    .roleType(UserRoleType.ROLE_SUPER_ADMIN)
                    .build();
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            User user = User.builder()
                    .firstName("Priyanshu")
                    .lastName("Yadav")
                    .email("priyansu.ydv.b@gmail.com")
                    .username("priyanshu@123")
                    .createdBy("SYSTEM")
                    .createdAt(new Date())
                    .password(new BCryptPasswordEncoder().encode("spring@123"))
                    .roles(roles)
                    .build();
            userRepository.save(user);
        }
    }
}
