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
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "feed_schedules")
public class FeedSchedule extends BaseEntity {

    @ManyToOne(optional = false)
    private Goat goat;

    @Column(name = "feed_name", nullable = false)
    private String feedName;

    @Column(name = "quantity_kg", nullable = false)
    private Double quantityKg;

    @Column(name = "scheduled_date", nullable = false)
    private LocalDate scheduledDate;

    @Column(name = "scheduled_time")
    private LocalTime scheduledTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FeedStatus status = FeedStatus.PLANNED;

    public enum FeedStatus {
        PLANNED,
        COMPLETED,
        MISSED
    }
}
