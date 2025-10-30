package com.farm.controller;

import com.farm.dto.InventoryItemDto;
import com.farm.service.InventoryService;
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
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','FARM_MANAGER','WORKER')")
    public InventoryItemDto create(@Valid @RequestBody InventoryItemDto dto, @AuthenticationPrincipal UserDetails userDetails) {
        return inventoryService.create(dto, userDetails.getUsername());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','FARM_MANAGER','WORKER')")
    public InventoryItemDto update(@PathVariable String id, @Valid @RequestBody InventoryItemDto dto, @AuthenticationPrincipal UserDetails userDetails) {
        return inventoryService.update(id, dto, userDetails.getUsername());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','FARM_MANAGER','WORKER','VIEWER')")
    public List<InventoryItemDto> list() {
        return inventoryService.list();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','FARM_MANAGER','WORKER','VIEWER')")
    public InventoryItemDto get(@PathVariable String id) {
        return inventoryService.get(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','FARM_MANAGER')")
    public void delete(@PathVariable String id) {
        inventoryService.delete(id);
    }
}
