package com.farm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UserDto(
        String id,
        @NotBlank String fullName,
        @Email String email,
        @NotBlank String username,
        @Size(min = 6, max = 50) String password,
        String phone,
        Set<String> roles,
        boolean enabled
) { }
