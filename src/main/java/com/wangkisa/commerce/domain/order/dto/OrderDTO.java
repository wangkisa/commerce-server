package com.wangkisa.commerce.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
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

        private List<RegisterOrderProduct> orderProductList;
    }

    @Getter
    @Builder
    @ToString
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
        private String id;

        @Schema(description = "수령인")
        private String receiverName;
        @Schema(description = "배송지")
        private String receiverAddress;
        @Schema(description = "기타 메시지")
        private String etcMessage;

        private List<RegisterOrderProduct> orderProductList;
    }
}
