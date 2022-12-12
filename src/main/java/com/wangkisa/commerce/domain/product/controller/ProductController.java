package com.wangkisa.commerce.domain.product.controller;

import com.wangkisa.commerce.domain.common.response.ApiResponse;
import com.wangkisa.commerce.domain.product.dto.ProductDto;
import com.wangkisa.commerce.domain.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/product")
@RestController
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 목록 조회")
    @PostMapping(value = "/getProductList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<ProductDto.ProductListResponseDTO> getProductList() {
        return ApiResponse.success(productService.getProductList());
    }

}