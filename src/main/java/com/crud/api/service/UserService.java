package com.crud.api.service;

import com.crud.api.dto.UserDto;
import com.crud.api.exception.ResourceNotFoundException;
import com.crud.api.exception.ValidationException;
import com.crud.api.mapper.UserMapper;
import com.crud.api.model.Role;
import com.crud.api.model.User;
import com.crud.api.repository.jpa.UserRepository;
import com.crud.api.repository.spec.UserSpecification;
import com.crud.api.type.UserRoleType;
import com.crud.api.utility.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSpecification userSpecification;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().filter(user -> !user.getCreatedBy().equals("SYSTEM")).map(userMapper::mapToDto).toList();
    }

    public UserDto getUserById(Long userId) {
        return userRepository.findById(userId).map(userMapper::mapToDto).orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));
    }

    public User getUserByUserName(String userName) {
        Specification<User> spec = userSpecification.userNameIs(userName);
        return userRepository.findOne(spec).orElse(null);
    }

    public User saveAdminUser(User user, String createdBy) throws Exception {
        boolean hasAdminOrUserRole = user.getRoles().stream()
                .anyMatch(role -> role.getRoleType() == UserRoleType.ROLE_ADMIN || role.getRoleType() == UserRoleType.ROLE_USER);
        if (getUserByUserName(user.getUsername()) == null) {
            if (hasAdminOrUserRole) {
                user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
                user.setCreatedBy(jwtTokenUtil.getUsernameFromToken(createdBy));
                return userRepository.save(user);
            } else {
                throw new ValidationException("User should must contains role!!");
            }
        } else {
            throw new ValidationException("User is already exists with provide username!");
        }
    }

    public UserDto saveUser(User user, String loggedInUser) {
        user.setCreatedBy(loggedInUser);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        Set<Role> roles = new HashSet<>();
        roles.add(Role.builder()
                .roleType(UserRoleType.ROLE_USER)
                .build());
        user.setRoles(roles);
        User updatedUser = userRepository.save(user);
        return userMapper.mapToDto(updatedUser);
    }

    public UserDto updateUser(Long userId, User user) {
        User existingUser =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));
        existingUser.setEmail(user.getEmail());
        existingUser.setLastName(user.getLastName());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setUpdatedAt(new Date());
        return userMapper.mapToDto(userRepository.save(existingUser));
    }

    public void deleteUser(Long userId) throws Exception {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));
        if (user.getCreatedBy().equals("SYSTEM")) {
            throw new ValidationException("Cannot delete system-created users");
        }
        userRepository.delete(user);
    }
}
