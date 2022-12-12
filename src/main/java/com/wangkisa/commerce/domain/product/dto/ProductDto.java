package com.wangkisa.commerce.domain.product.dto;

import com.wangkisa.commerce.domain.product.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

public class ProductDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductListResponseDTO {

        @Schema(description = "상품 목록")
        List<ResDefaultList> productList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResDefaultList {

        @Schema(description = "상품 아이디")
        private Long productId;
        @Schema(description = "상품 이름")
        private String name;
        @Schema(description = "상품 가격")
        private BigDecimal price;

        public static ResDefaultList fromProduct(Product product) {
            return ResDefaultList.builder()
                    .productId(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .build();
        }
    }
}
