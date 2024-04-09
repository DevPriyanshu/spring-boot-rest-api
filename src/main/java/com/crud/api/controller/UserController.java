package com.crud.api.controller;

import com.crud.api.annotations.HasRole;
import com.crud.api.annotations.*;
import com.crud.api.dto.UserDto;
import com.crud.api.mapper.UserMapper;
import com.crud.api.model.User;
import com.crud.api.repository.jpa.UserRepository;
import com.crud.api.service.UserService;
import com.crud.api.type.UserRoleType;
import com.crud.api.utility.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    @accessForSuperAdmin
    public List<UserDto> getAllUsers(@RequestHeader Map<String, String> headers) {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    @accessForSuperAdmin
    public ResponseEntity<UserDto> getUsersById(@PathVariable(value = "id") Long userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @PostMapping("/users")
    @accessForAdmin
    public ResponseEntity<UserDto> createUser(@RequestBody User user, @RequestHeader Map<String, String> headers) {
        String[] jwt = headers.get("authorization").split(" ");
        user.setCreatedBy(jwtTokenUtil.getUsernameFromToken(jwt[1]));
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }

    @PutMapping("/users/{id}")
    @accessForAdmin
    @HasRole({UserRoleType.ROLE_ADMIN, UserRoleType.ROLE_USER})
    public ResponseEntity<UserDto> updateUser(
            @PathVariable(value = "id") Long userId, @RequestBody User userDetails, @RequestHeader Map<String, String> headers) {
        String[] jwt = headers.get("authorization").split(" ");
        userDetails.setUpdatedBy(jwtTokenUtil.getUsernameFromToken(jwt[1]));
        return new ResponseEntity<>(userService.updateUser(userId, userDetails), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/user/{id}")
    @accessForSuperAdmin
    @HasRole({UserRoleType.ROLE_ADMIN})
    public ResponseEntity<?> deleteUser(@PathVariable(value = "id") Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
