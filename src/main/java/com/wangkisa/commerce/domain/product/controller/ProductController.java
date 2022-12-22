package com.wangkisa.commerce.domain.product.controller;

import com.wangkisa.commerce.domain.common.response.ApiResponse;
import com.wangkisa.commerce.domain.product.dto.ProductDTO;
import com.wangkisa.commerce.domain.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/product")
@RestController
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 목록 조회")
    @PostMapping(value = "/getProductList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<ProductDTO.ResProductList> getProductList() {
        return ApiResponse.success(productService.getProductList());
    }

    @Operation(summary = "상품 상세 조회")
    @PostMapping(value = "/getProductDetail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<ProductDTO.ResProductDetail> getProductDetail(@Validated @RequestBody ProductDTO.ReqProductDetail reqProductDetail) {
        return ApiResponse.success(productService.getProductDetail(reqProductDetail));
    }

}
