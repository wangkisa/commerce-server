package com.wangkisa.commerce.domain.order.entity;

import com.wangkisa.commerce.domain.common.entity.BaseEntity;
import com.wangkisa.commerce.domain.order.code.OrderErrorCode;
import com.wangkisa.commerce.domain.product.entity.Product;
import com.wangkisa.commerce.domain.user.entity.User;
import com.wangkisa.commerce.exception.CustomException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static javax.persistence.CascadeType.PERSIST;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(name = "orders", indexes = {})
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "uuid-char")
    @Column(name = "order_id", columnDefinition = "varchar(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDateTime orderDateTime;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = {PERSIST})
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @Builder
    public Order (User user) {
        if (user == null) {
            throw new CustomException(OrderErrorCode.ERROR_NOT_FOUND_USER);
        }

        this.user = user;
        this.orderDateTime = LocalDateTime.now();
        this.orderStatus = OrderStatus.ORDER_INIT;
    }

    public OrderProduct addOrderProduct(Product product, Integer productQuantity, BigDecimal totalPrice) {
        OrderProduct orderProduct = OrderProduct.builder()
                .order(this)
                .product(product)
                .productName(product.getName())
                .productQuantity(productQuantity)
                .productPrice(product.getPrice())
                .totalPrice(totalPrice)
                .build();
        this.orderProducts.add(orderProduct);
        return orderProduct;
    }
}
