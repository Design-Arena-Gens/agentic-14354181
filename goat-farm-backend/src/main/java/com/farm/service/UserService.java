package com.farm.service;

import com.farm.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto register(UserDto dto, String createdBy);
    UserDto update(String id, UserDto dto, String updatedBy);
    List<UserDto> list();
    UserDto get(String id);
    void disable(String id, boolean enabled);
}
