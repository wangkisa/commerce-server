package com.wangkisa.commerce.domain.order.entity;

import com.wangkisa.commerce.domain.common.entity.BaseEntity;
import com.wangkisa.commerce.domain.product.entity.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.math.BigDecimal;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(name = "order_product", indexes = {})
public class OrderProduct extends BaseEntity {

    @Id
    @Column(name = "order_product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer productQuantity;

    private String productName;
    @Column(precision = 10, scale = 2)
    private BigDecimal productPrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Builder
    public OrderProduct(Order order, Product product, Integer productQuantity, String productName, BigDecimal productPrice, BigDecimal totalPrice) {
        this.order = order;
        this.product = product;
        this.productQuantity = productQuantity;
        this.productName = productName;
        this.productPrice = productPrice;
        this.totalPrice = totalPrice;
    }
}
