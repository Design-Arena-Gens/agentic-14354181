package com.farm.controller;

import com.farm.dto.HealthRecordDto;
import com.farm.service.HealthService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    private final HealthService healthService;

    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','VETERINARIAN','FARM_MANAGER')")
    public HealthRecordDto create(@Valid @RequestBody HealthRecordDto dto, @AuthenticationPrincipal UserDetails userDetails) {
        return healthService.create(dto, userDetails.getUsername());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','VETERINARIAN','FARM_MANAGER')")
    public HealthRecordDto update(@PathVariable String id, @Valid @RequestBody HealthRecordDto dto, @AuthenticationPrincipal UserDetails userDetails) {
        return healthService.update(id, dto, userDetails.getUsername());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','VETERINARIAN','FARM_MANAGER','VIEWER')")
    public List<HealthRecordDto> list() {
        return healthService.list();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','VETERINARIAN','FARM_MANAGER','VIEWER')")
    public HealthRecordDto get(@PathVariable String id) {
        return healthService.get(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','VETERINARIAN','FARM_MANAGER')")
    public void delete(@PathVariable String id) {
        healthService.delete(id);
    }
}
