package com.farm.controller;

import com.farm.dto.ExpenseDto;
import com.farm.service.ExpenseService;
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
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ACCOUNTANT','FARM_MANAGER')")
    public ExpenseDto create(@Valid @RequestBody ExpenseDto dto, @AuthenticationPrincipal UserDetails userDetails) {
        return expenseService.create(dto, userDetails.getUsername());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ACCOUNTANT','FARM_MANAGER')")
    public ExpenseDto update(@PathVariable String id, @Valid @RequestBody ExpenseDto dto, @AuthenticationPrincipal UserDetails userDetails) {
        return expenseService.update(id, dto, userDetails.getUsername());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ACCOUNTANT','FARM_MANAGER','VIEWER')")
    public List<ExpenseDto> list() {
        return expenseService.list();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ACCOUNTANT','FARM_MANAGER','VIEWER')")
    public ExpenseDto get(@PathVariable String id) {
        return expenseService.get(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ACCOUNTANT')")
    public void delete(@PathVariable String id) {
        expenseService.delete(id);
    }
}
