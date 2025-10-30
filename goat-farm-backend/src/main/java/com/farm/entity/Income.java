package com.farm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "income")
public class Income extends BaseEntity {

    @Column(name = "income_date", nullable = false)
    private LocalDate incomeDate;

    @Column(name = "source", nullable = false)
    private String source;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "description", length = 1000)
    private String description;
}
