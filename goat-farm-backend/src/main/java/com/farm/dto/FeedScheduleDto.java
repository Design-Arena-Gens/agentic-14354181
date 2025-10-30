package com.farm.dto;

import com.farm.entity.FeedSchedule;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record FeedScheduleDto(
        String id,
        @NotBlank String goatId,
        @NotBlank String feedName,
        @NotNull Double quantityKg,
        @NotNull LocalDate scheduledDate,
        LocalTime scheduledTime,
        FeedSchedule.FeedStatus status
) { }
