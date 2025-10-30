package com.farm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SalesRecordDto(
        String id,
        @NotBlank String goatId,
        @NotBlank String buyerName,
        @NotNull LocalDate saleDate,
        @NotNull Double salePrice,
        String paymentStatus,
        String remarks
) { }
