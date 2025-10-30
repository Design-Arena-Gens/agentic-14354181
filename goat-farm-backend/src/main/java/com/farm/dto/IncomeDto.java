package com.farm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record IncomeDto(
        String id,
        @NotNull LocalDate incomeDate,
        @NotBlank String source,
        @NotNull Double amount,
        String description
) { }
