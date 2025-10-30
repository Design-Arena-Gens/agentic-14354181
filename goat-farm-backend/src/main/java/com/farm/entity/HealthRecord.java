package com.farm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "health_records")
public class HealthRecord extends BaseEntity {

    @ManyToOne(optional = false)
    private Goat goat;

    @Enumerated(EnumType.STRING)
    @Column(name = "record_type", nullable = false)
    private HealthRecordType recordType;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "vaccine_or_medicine")
    private String vaccineOrMedicine;

    @Column(name = "dosage")
    private String dosage;

    @Column(name = "veterinarian")
    private String veterinarian;

    @Column(name = "notes", length = 1000)
    private String notes;

    public enum HealthRecordType {
        VACCINATION,
        DEWORMING,
        TREATMENT,
        CHECKUP,
        QUARANTINE
    }
}
