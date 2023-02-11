package com.wangkisa.commerce.domain.product.entity;

import com.wangkisa.commerce.domain.common.entity.BaseEntity;
import com.wangkisa.commerce.domain.product.code.ProductErrorCode;
import com.wangkisa.commerce.exception.CustomException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import javax.persistence.*;
import java.math.BigDecimal;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(name = "product", indexes = {})
@Check(constraints = "quantity >= 0")
public class Product extends BaseEntity {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String color;

    private int quantity;

    @Column(precision = 10, scale = 0)
    private BigDecimal price;

    @Version
    private Long version;

    @Builder
    public Product(String name, String color, Integer quantity, BigDecimal price) {
        this.name = name;
        this.color = color;
        this.quantity = quantity;
        this.price = price;
    }

    // 상품 수량 체크
    public void checkQuantity(Integer reqQuantity) {

        if (this.quantity == 0) {
            throw new CustomException(ProductErrorCode.ERROR_NONE_OF_PRODUCT_QUANTITY);
        }
        else if (reqQuantity > this.quantity ) {
            throw new CustomException(ProductErrorCode.ERROR_LACK_OF_PRODUCT_QUANTITY);
        }
    }

    // 상품 수량 차감
    public void subtractQuantity(Integer quantity) {
        if (this.quantity - quantity < 0) {
            throw new CustomException(ProductErrorCode.ERROR_ILLEGAL_REQUEST_QUANTITY);
        }
        this.quantity -= quantity;
    }
}
