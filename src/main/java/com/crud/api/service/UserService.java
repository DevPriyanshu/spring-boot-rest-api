package com.crud.api.service;

import com.crud.api.dto.UserDto;
import com.crud.api.exception.ResourceNotFoundException;
import com.crud.api.mapper.UserMapper;
import com.crud.api.model.User;
import com.crud.api.repository.jpa.UserRepository;
import com.crud.api.repository.spec.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSpecification userSpecification;
    @Autowired
    private UserMapper userMapper;

    public List<UserDto> getAllUsers() {
        List<UserDto> userDto = userRepository.findAll().stream().map(userMapper::mapToDto).toList();
        return userDto;
    }

    public UserDto getUserById(Long userId) {
        UserDto userDto = userRepository.findById(userId).map(userMapper::mapToDto).orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));
        return userDto;
    }

    public UserDto saveUser(User user) {
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

    public void deleteUser(Long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));
        userRepository.delete(user);
    }
}
