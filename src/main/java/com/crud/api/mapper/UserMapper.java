package com.crud.api.mapper;

import com.crud.api.dto.UserDto;
import com.crud.api.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    public UserDto mapToDto(User source){
        return source == null ? null : this.modelMapper.map(source, UserDto.class);
    }
    public User mapToEntity(UserDto source){
        return source == null ? null : this.modelMapper.map(source, User.class);
    }
}
