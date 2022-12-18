package com.wangkisa.commerce.domain.order.entity;

import com.wangkisa.commerce.domain.common.entity.EnumInterface;

import java.util.Arrays;

public enum OrderStatus implements EnumInterface {
    ORDER_INIT("배송 준비", 0),
    ORDER_COMPLETE("주문 완료", 1),
    DELIVERY_READY("배송 준비", 2),
    IN_DELIVERY("배송중", 3),
    DELIVERY_COMPLETE("배송 완료", 4),
    REFUND("환불", 5),
    ;

    private final String name;
    private final int code;

    OrderStatus(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public static OrderStatus valueOf(int code) {
        return Arrays.stream(OrderStatus.values())
                .filter(status -> status.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Unsupported code %s.", code)));
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
