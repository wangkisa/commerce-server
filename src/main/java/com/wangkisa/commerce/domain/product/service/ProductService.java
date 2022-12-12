package com.wangkisa.commerce.domain.product.service;

import com.wangkisa.commerce.domain.product.dto.ProductDto;
import com.wangkisa.commerce.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    ProductRepository productRepository;

    /**
     * 상품 목록 조회
     */
    @Transactional(readOnly = true)
    public ProductDto.ProductListResponseDTO getProductList() {
        List<ProductDto.ResDefaultList> productList = productRepository.findAll().stream().map(product ->
                ProductDto.ResDefaultList.fromProduct(product)).collect(Collectors.toList());
        return ProductDto.ProductListResponseDTO.builder()
                .productList(productList)
                .build();
    }
}
