package com.wangkisa.commerce.domain.product.entity;

import com.wangkisa.commerce.domain.common.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(name = "product", indexes = {})
public class Product extends BaseEntity {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String color;

    private Integer quantity;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Builder
    public Product(String name, String color, Integer quantity, BigDecimal price) {
        this.name = name;
        this.color = color;
        this.quantity = quantity;
        this.price = price;
    }
}
