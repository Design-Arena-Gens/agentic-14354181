package com.farm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BreedingRecordDto(
        String id,
        @NotBlank String doeId,
        @NotBlank String buckId,
        @NotNull LocalDate breedingDate,
        LocalDate expectedKiddingDate,
        LocalDate actualKiddingDate,
        Integer kidsBorn,
        String remarks
) { }
