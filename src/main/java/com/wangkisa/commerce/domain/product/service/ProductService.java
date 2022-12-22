package com.wangkisa.commerce.domain.product.service;

import com.wangkisa.commerce.domain.product.code.ProductErrorCode;
import com.wangkisa.commerce.domain.product.dto.ProductDTO;
import com.wangkisa.commerce.domain.product.entity.Product;
import com.wangkisa.commerce.domain.product.repository.ProductRepository;
import com.wangkisa.commerce.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 상품 목록 조회
     */
    @Transactional(readOnly = true)
    public ProductDTO.ResProductList getProductList() {
        List<ProductDTO.ResDefaultList> productList = productRepository.findAll().stream().map(product ->
                ProductDTO.ResDefaultList.fromProduct(product)).collect(Collectors.toList());
        return ProductDTO.ResProductList.builder()
                .productList(productList)
                .build();
    }

    /**
     * 상품 상세 조회
     */
    @Transactional(readOnly = true)
    public ProductDTO.ResProductDetail getProductDetail(ProductDTO.ReqProductDetail reqProductDetail) {

        Product product = productRepository.findById(reqProductDetail.getProductId())
                .orElseThrow(() -> new CustomException(ProductErrorCode.ERROR_NOT_FOUND_PRODUCT));

        return ProductDTO.ResProductDetail.fromProduct(product);
    }
}
