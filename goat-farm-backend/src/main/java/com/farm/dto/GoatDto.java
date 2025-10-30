package com.farm.dto;

import com.farm.entity.Goat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record GoatDto(
        String id,
        @NotBlank String tagId,
        String name,
        @NotNull Goat.Gender gender,
        @NotBlank String breed,
        LocalDate dateOfBirth,
        Double weightKg,
        Double bodyConditionScore,
        String motherId,
        String fatherId,
        String status
) { }
