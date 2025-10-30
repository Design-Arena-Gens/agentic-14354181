package com.farm.config;

import com.farm.entity.Role;
import com.farm.entity.User;
import com.farm.repository.RoleRepository;
import com.farm.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;

@Configuration
@Profile("!test")
public class DataInitializer {

    @Bean
    CommandLineRunner seedRolesAndAdmin(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Arrays.stream(Role.RoleName.values()).forEach(roleName -> {
                roleRepository.findByName(roleName).orElseGet(() -> {
                    Role role = new Role();
                    role.setName(roleName);
                    role.setCreatedBy("system");
                    role.setUpdatedBy("system");
                    return roleRepository.save(role);
                });
            });

            if (userRepository.findByUsername("admin").isEmpty()) {
                Role superAdminRole = roleRepository.findByName(Role.RoleName.SUPER_ADMIN)
                        .orElseThrow();
                User admin = new User();
                admin.setFullName("System Administrator");
                admin.setEmail("admin@goatfarm.local");
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("Admin@123"));
                admin.setRoles(new HashSet<>(java.util.List.of(superAdminRole)));
                admin.setCreatedBy("system");
                admin.setUpdatedBy("system");
                userRepository.save(admin);
            }
        };
    }
}
