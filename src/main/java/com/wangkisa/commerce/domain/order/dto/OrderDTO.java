package com.wangkisa.commerce.domain.order.dto;

import com.wangkisa.commerce.domain.order.entity.Order;
import com.wangkisa.commerce.domain.order.entity.OrderProduct;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class OrderDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReqRegisterOrder {

        @Schema(description = "수령인")
        private String receiverName;
        @Schema(description = "배송지")
        private String receiverAddress;
        @Schema(description = "기타 메시지")
        private String etcMessage;

        private List<RegisterOrderProduct> orderProductList = new ArrayList<>();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterOrderProduct {

        @Schema(description = "상품 아이디")
        @NotNull(message = "상품 아이디를 입력하세요.")
        private Long productId;

        @Schema(description = "상품 수량")
        private Integer productQuantity;
        @Schema(description = "상품 이름")
        private String productName;
        @Schema(description = "상품 가격")
        private Long productPrice;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResOrderInfo {

        @Schema(description = "주문 아이디")
        private String orderId;

        @Schema(description = "수령인")
        private String receiverName;
        @Schema(description = "배송지")
        private String receiverAddress;
        @Schema(description = "기타 메시지")
        private String etcMessage;

        private List<OrderProductInfo> orderProductList = new ArrayList<>();

        public static ResOrderInfo fromOrder(Order order, List<OrderProductInfo> orderProductList) {
            return ResOrderInfo.builder()
                    .orderId(order.getId().toString())
                    .receiverName(order.getDeliveryInfo().getReceiverName())
                    .receiverAddress(order.getDeliveryInfo().getReceiverAddress())
                    .etcMessage(order.getDeliveryInfo().getEtcMessage())
                    .orderProductList(orderProductList)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderProductInfo {

        @Schema(description = "상품 아이디")
        @NotNull(message = "상품 아이디를 입력하세요.")
        private Long productId;

        @Schema(description = "상품 수량")
        private Integer productQuantity;
        @Schema(description = "상품 이름")
        private String productName;
        @Schema(description = "상품 가격")
        private Long productPrice;

        @Schema(description = "상품 총 가격")
        private Long totalPrice;

        public static OrderProductInfo fromOrderProduct(OrderProduct orderProduct) {
            return OrderProductInfo.builder()
                    .productId(orderProduct.getProduct().getId())
                    .productPrice(orderProduct.getProductPrice().longValue())
                    .productName(orderProduct.getProductName())
                    .totalPrice(orderProduct.getTotalPrice().longValue())
                    .productQuantity(orderProduct.getProductQuantity())
                    .build();
        }
    }
}
