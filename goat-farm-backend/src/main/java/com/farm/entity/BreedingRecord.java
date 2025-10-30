package com.farm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "breeding_records")
public class BreedingRecord extends BaseEntity {

    @ManyToOne(optional = false)
    private Goat doe;

    @ManyToOne(optional = false)
    private Goat buck;

    @Column(name = "breeding_date", nullable = false)
    private LocalDate breedingDate;

    @Column(name = "expected_kidding_date")
    private LocalDate expectedKiddingDate;

    @Column(name = "actual_kidding_date")
    private LocalDate actualKiddingDate;

    @Column(name = "kids_born")
    private Integer kidsBorn;

    @Column(name = "remarks", length = 1000)
    private String remarks;
}
