package com.wangkisa.commerce.domain.product.dto;

import com.wangkisa.commerce.domain.product.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResProductList {

        @Schema(description = "상품 목록")
        List<ResDefaultList> productList;

        @Schema(description = "전체 개수")
        long totalCount;
        @Schema(description = "다음 페이지 유무")
        boolean hasNext;

        public static ResProductList fromPageProductList(Page<Product> productPage) {
            System.out.println("productPage = " + productPage);
            List<ResDefaultList> productList = productPage.getContent().stream().map(product -> ResDefaultList.fromProduct(product))
                    .collect(Collectors.toList());
            return ResProductList.builder()
                    .productList(productList)
                    .totalCount(productPage.getTotalElements())
                    .hasNext(productPage.hasNext())
                    .build();
//                    (productPage.getContent(), productPage.getTotalElements(), productPage.hasNext());
        }
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

        @Schema(description = "상품 수량")
        private int quantity;

        public static ResDefaultList fromProduct(Product product) {
            return ResDefaultList.builder()
                    .productId(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .quantity(product.getQuantity())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReqProductDetail {

        @Schema(description = "상품 아이디")
        private Long productId;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResProductDetail {

        @Schema(description = "상품 아이디")
        private Long productId;
        @Schema(description = "상품 이름")
        private String name;
        @Schema(description = "상품 가격")
        private BigDecimal price;
        @Schema(description = "상품 색깔")
        private String color;

        public static ResProductDetail fromProduct(Product product) {
            return ResProductDetail.builder()
                    .productId(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .color(product.getColor())
                    .build();
        }
    }
}
