package com.farm.controller;

import com.farm.dto.BreedingRecordDto;
import com.farm.service.BreedingService;
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
@RequestMapping("/api/breeding")
public class BreedingController {

    private final BreedingService breedingService;

    public BreedingController(BreedingService breedingService) {
        this.breedingService = breedingService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','FARM_MANAGER')")
    public BreedingRecordDto create(@Valid @RequestBody BreedingRecordDto dto, @AuthenticationPrincipal UserDetails userDetails) {
        return breedingService.create(dto, userDetails.getUsername());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','FARM_MANAGER')")
    public BreedingRecordDto update(@PathVariable String id, @Valid @RequestBody BreedingRecordDto dto, @AuthenticationPrincipal UserDetails userDetails) {
        return breedingService.update(id, dto, userDetails.getUsername());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','FARM_MANAGER','VIEWER')")
    public List<BreedingRecordDto> list() {
        return breedingService.list();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','FARM_MANAGER','VIEWER')")
    public BreedingRecordDto get(@PathVariable String id) {
        return breedingService.get(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','FARM_MANAGER')")
    public void delete(@PathVariable String id) {
        breedingService.delete(id);
    }
}
