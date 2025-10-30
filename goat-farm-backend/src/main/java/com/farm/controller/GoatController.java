package com.farm.controller;

import com.farm.dto.GoatDto;
import com.farm.service.GoatService;
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
@RequestMapping("/api/goats")
public class GoatController {

    private final GoatService goatService;

    public GoatController(GoatService goatService) {
        this.goatService = goatService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','FARM_MANAGER','WORKER')")
    public GoatDto create(@Valid @RequestBody GoatDto dto, @AuthenticationPrincipal UserDetails userDetails) {
        return goatService.create(dto, userDetails.getUsername());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','FARM_MANAGER','WORKER')")
    public GoatDto update(@PathVariable String id, @Valid @RequestBody GoatDto dto, @AuthenticationPrincipal UserDetails userDetails) {
        return goatService.update(id, dto, userDetails.getUsername());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','FARM_MANAGER','VETERINARIAN','VIEWER','WORKER')")
    public List<GoatDto> list() {
        return goatService.list();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','FARM_MANAGER','VETERINARIAN','VIEWER','WORKER')")
    public GoatDto get(@PathVariable String id) {
        return goatService.get(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','FARM_MANAGER')")
    public void delete(@PathVariable String id) {
        goatService.delete(id);
    }
}
