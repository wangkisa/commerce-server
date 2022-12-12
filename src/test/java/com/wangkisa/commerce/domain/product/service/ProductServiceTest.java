package com.wangkisa.commerce.domain.product.service;

import com.wangkisa.commerce.domain.product.dto.ProductDto;
import com.wangkisa.commerce.domain.product.entity.Product;
import com.wangkisa.commerce.domain.product.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Test
    @DisplayName("상품 목록 조회 테스트")
    @Transactional
    void productListTest() {
        // given
        Product mockProduct = createProductMock();
        // when
        ProductDto.ProductListResponseDTO productList = productService.getProductList();
        // then
        Assertions.assertThat(productList.getProductList().size()).isEqualTo(1);
        Assertions.assertThat(productList.getProductList().get(0).getName()).isEqualTo(mockProduct.getName());
        Assertions.assertThat(productList.getProductList().get(0).getPrice()).isEqualTo(mockProduct.getPrice());
    }
    @Transactional
    private Product createProductMock() {
        return productRepository.save(Product.builder()
                .name("테스트 상품")
                .color("red")
                .quantity(2)
                .price(BigDecimal.valueOf(2000))
                .build());
    }
}