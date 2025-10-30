package com.farm.controller;

import com.farm.dto.SalesRecordDto;
import com.farm.service.SalesService;
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
@RequestMapping("/api/sales")
public class SalesController {

    private final SalesService salesService;

    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','FARM_MANAGER','ACCOUNTANT')")
    public SalesRecordDto create(@Valid @RequestBody SalesRecordDto dto, @AuthenticationPrincipal UserDetails userDetails) {
        return salesService.create(dto, userDetails.getUsername());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','FARM_MANAGER','ACCOUNTANT')")
    public SalesRecordDto update(@PathVariable String id, @Valid @RequestBody SalesRecordDto dto, @AuthenticationPrincipal UserDetails userDetails) {
        return salesService.update(id, dto, userDetails.getUsername());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','FARM_MANAGER','ACCOUNTANT','VIEWER')")
    public List<SalesRecordDto> list() {
        return salesService.list();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','FARM_MANAGER','ACCOUNTANT','VIEWER')")
    public SalesRecordDto get(@PathVariable String id) {
        return salesService.get(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','FARM_MANAGER')")
    public void delete(@PathVariable String id) {
        salesService.delete(id);
    }
}
