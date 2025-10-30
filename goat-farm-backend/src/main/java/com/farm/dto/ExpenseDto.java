package com.farm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ExpenseDto(
        String id,
        @NotNull LocalDate expenseDate,
        @NotBlank String category,
        @NotNull Double amount,
        String description,
        String vendor
) { }
