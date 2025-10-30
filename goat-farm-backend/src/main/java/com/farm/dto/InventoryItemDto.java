package com.farm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record InventoryItemDto(
        String id,
        @NotBlank String itemName,
        @NotBlank String category,
        @NotNull Integer quantity,
        String unit,
        LocalDate purchaseDate,
        LocalDate expiryDate,
        Double unitCost,
        String supplier
) { }
