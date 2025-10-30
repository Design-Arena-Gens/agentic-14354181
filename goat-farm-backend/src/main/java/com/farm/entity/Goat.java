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
@Table(name = "goats")
public class Goat extends BaseEntity {

    @Column(name = "tag_id", nullable = false, unique = true)
    private String tagId;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "breed", nullable = false)
    private String breed;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "weight_kg")
    private Double weightKg;

    @Column(name = "body_condition_score")
    private Double bodyConditionScore;

    @ManyToOne
    private Goat mother;

    @ManyToOne
    private Goat father;

    @Column(name = "status")
    private String status;

    public enum Gender {
        DOE,
        BUCK
    }
}
