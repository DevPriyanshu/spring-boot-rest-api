package com.crud.api.dto;

import com.crud.api.model.Role;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class UserDto {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private Date createdAt;
    private String createdBy;
    private Date updatedAt;
    private String updatedBy;
    private Set<Role> roles;
}
