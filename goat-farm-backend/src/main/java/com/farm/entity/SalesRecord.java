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
@Table(name = "sales_records")
public class SalesRecord extends BaseEntity {

    @ManyToOne(optional = false)
    private Goat goat;

    @Column(name = "buyer_name", nullable = false)
    private String buyerName;

    @Column(name = "sale_date", nullable = false)
    private LocalDate saleDate;

    @Column(name = "sale_price", nullable = false)
    private Double salePrice;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;

    @Column(name = "remarks", length = 1000)
    private String remarks;
}
