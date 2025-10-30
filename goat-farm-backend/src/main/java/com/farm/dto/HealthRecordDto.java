package com.farm.dto;

import com.farm.entity.HealthRecord;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record HealthRecordDto(
        String id,
        @NotBlank String goatId,
        @NotNull HealthRecord.HealthRecordType recordType,
        @NotNull LocalDate recordDate,
        String vaccineOrMedicine,
        String dosage,
        String veterinarian,
        String notes
) { }
