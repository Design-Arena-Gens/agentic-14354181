package com.farm.controller;

import com.farm.dto.IncomeDto;
import com.farm.service.IncomeService;
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
@RequestMapping("/api/income")
public class IncomeController {

    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ACCOUNTANT','FARM_MANAGER')")
    public IncomeDto create(@Valid @RequestBody IncomeDto dto, @AuthenticationPrincipal UserDetails userDetails) {
        return incomeService.create(dto, userDetails.getUsername());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ACCOUNTANT','FARM_MANAGER')")
    public IncomeDto update(@PathVariable String id, @Valid @RequestBody IncomeDto dto, @AuthenticationPrincipal UserDetails userDetails) {
        return incomeService.update(id, dto, userDetails.getUsername());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ACCOUNTANT','FARM_MANAGER','VIEWER')")
    public List<IncomeDto> list() {
        return incomeService.list();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ACCOUNTANT','FARM_MANAGER','VIEWER')")
    public IncomeDto get(@PathVariable String id) {
        return incomeService.get(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ACCOUNTANT')")
    public void delete(@PathVariable String id) {
        incomeService.delete(id);
    }
}
