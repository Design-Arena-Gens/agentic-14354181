package com.farm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "report_logs")
public class ReportLog extends BaseEntity {

    @Column(name = "report_name", nullable = false)
    private String reportName;

    @Column(name = "format", nullable = false)
    private String format;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "triggered_by")
    private String triggeredBy;

    @Column(name = "delivery_channel")
    private String deliveryChannel;

    @Column(name = "notes", length = 1000)
    private String notes;
}
