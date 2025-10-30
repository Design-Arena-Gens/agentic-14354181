package com.farm.dto;

import jakarta.validation.constraints.NotBlank;

public record ReportLogDto(
        String id,
        @NotBlank String reportName,
        @NotBlank String format,
        String status,
        String triggeredBy,
        String deliveryChannel,
        String notes
) { }
