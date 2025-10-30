package com.farm.service.impl;

import com.farm.dto.UserDto;
import com.farm.entity.Role;
import com.farm.entity.User;
import com.farm.exception.BadRequestException;
import com.farm.exception.ResourceNotFoundException;
import com.farm.repository.RoleRepository;
import com.farm.repository.UserRepository;
import com.farm.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto register(UserDto dto, String createdBy) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new BadRequestException("Email already in use");
        }
        if (userRepository.existsByUsername(dto.username())) {
            throw new BadRequestException("Username already in use");
        }
        User user = new User();
        copyToEntity(dto, user);
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setCreatedBy(createdBy);
        user.setUpdatedBy(createdBy);
        user.setRoles(resolveRoles(dto.roles()));
        User saved = userRepository.save(user);
        return toDto(saved);
    }

    @Override
    public UserDto update(String id, UserDto dto, String updatedBy) {
        User user = getEntity(id);
        copyToEntity(dto, user);
        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }
        user.setUpdatedBy(updatedBy);
        if (dto.roles() != null && !dto.roles().isEmpty()) {
            user.setRoles(resolveRoles(dto.roles()));
        }
        user.setEnabled(dto.enabled());
        return toDto(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> list() {
        return userRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto get(String id) {
        return toDto(getEntity(id));
    }

    @Override
    public void disable(String id, boolean enabled) {
        User user = getEntity(id);
        user.setEnabled(enabled);
        userRepository.save(user);
    }

    private User getEntity(String id) {
        return userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Set<Role> resolveRoles(Set<String> names) {
        if (names == null || names.isEmpty()) {
            Role defaultRole = roleRepository.findByName(Role.RoleName.VIEWER)
                    .orElseThrow(() -> new ResourceNotFoundException("Default role not configured"));
            return Set.of(defaultRole);
        }
        return names.stream()
                .map(name -> roleRepository.findByName(Role.RoleName.valueOf(name))
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + name)))
                .collect(Collectors.toSet());
    }

    private UserDto toDto(User user) {
        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
        return new UserDto(
                user.getId().toString(),
                user.getFullName(),
                user.getEmail(),
                user.getUsername(),
                null,
                user.getPhone(),
                roles,
                user.isEnabled()
        );
    }

    private void copyToEntity(UserDto dto, User user) {
        user.setFullName(dto.fullName());
        user.setEmail(dto.email());
        user.setUsername(dto.username());
        user.setPhone(dto.phone());
    }
}
