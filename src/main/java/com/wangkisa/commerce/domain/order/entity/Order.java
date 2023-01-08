package com.wangkisa.commerce.domain.order.entity;

import com.wangkisa.commerce.domain.common.entity.BaseEntity;
import com.wangkisa.commerce.domain.common.util.BigDecimalUtil;
import com.wangkisa.commerce.domain.order.code.OrderErrorCode;
import com.wangkisa.commerce.domain.product.entity.Product;
import com.wangkisa.commerce.domain.user.code.UserErrorCode;
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
    @Column(name = "order_id", columnDefinition = "varchar(55)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDateTime orderDateTime;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private DeliveryInfo deliveryInfo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = {PERSIST})
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @Builder
    public Order (User user, DeliveryInfo deliveryInfo) {
        if (user == null) {
            throw new CustomException(UserErrorCode.ERROR_NOT_FOUND_USER);
        }

        this.user = user;
        this.orderDateTime = LocalDateTime.now();
        this.orderStatus = OrderStatus.ORDER_INIT;
        this.deliveryInfo = deliveryInfo;
    }

    public OrderProduct addOrderProduct(Product product, int productQuantity) {
        // 제품 수량 체크
        product.checkQuantity(productQuantity);

        OrderProduct orderProduct = OrderProduct.builder()
                .order(this)
                .product(product)
                .productName(product.getName())
                .productQuantity(productQuantity)
                .productPrice(product.getPrice())
                .totalPrice(BigDecimalUtil.multiply(product.getPrice(), productQuantity))
                .build();
        this.orderProducts.add(orderProduct);
        return orderProduct;
    }

    // 주문 총 금액 조회
    public BigDecimal getOrderTotalPrice() {
        return this.getOrderProducts().stream()
                .map(orderProduct -> orderProduct.getTotalPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // 구매 프로세스
    public void purchase() {
        if (!this.orderStatus.equals(OrderStatus.ORDER_INIT)) {
            throw new CustomException(OrderErrorCode.ERROR_INVALID_ORDER_STATUS);
        }
        // 유저 포인트 주문 총 금액만큼 차감
        this.user.subtractPoint(this.getOrderTotalPrice());
        // 상품 재고 수량 주문 수량만큼 차감
        this.orderProducts.forEach(orderProduct ->
                orderProduct.getProduct().subtractQuantity(
                        orderProduct.getProductQuantity()
                ));
        // 주문 완료 상태로 변경
        this.orderStatus = OrderStatus.ORDER_COMPLETE;
    }
}
