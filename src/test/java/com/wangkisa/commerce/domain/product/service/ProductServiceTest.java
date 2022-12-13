package com.wangkisa.commerce.domain.product.service;

import com.wangkisa.commerce.domain.product.code.ProductErrorCode;
import com.wangkisa.commerce.domain.product.dto.ProductDto;
import com.wangkisa.commerce.domain.product.entity.Product;
import com.wangkisa.commerce.domain.product.repository.ProductRepository;
import com.wangkisa.commerce.domain.user.code.UserErrorCode;
import com.wangkisa.commerce.exception.CustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
        ProductDto.ResProductList productList = productService.getProductList();
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

    @Test
    @DisplayName("상품 상세 조회 알수없는 상품아이디로 실패 테스트")
    @Transactional
    void productDetailNotFoundTest() {
        // given
        Product mockProduct = createProductMock();
        // 상품 아이디에 1을 더해서 엉뚱한 값으로 할당
        ProductDto.ReqProductDetail reqProductDetail = ProductDto.ReqProductDetail.builder()
                .productId(mockProduct.getId() + 1)
                .build();

        // when
        CustomException customException = assertThrows(CustomException.class, () -> productService.getProductDetail(reqProductDetail));

        // then
        Assertions.assertThat(customException.getCode()).isEqualTo(ProductErrorCode.ERROR_NOT_FOUND_PRODUCT.getCode());
        Assertions.assertThat(customException.getMessage()).isEqualTo(ProductErrorCode.ERROR_NOT_FOUND_PRODUCT.getMsg());
    }

    @Test
    @DisplayName("상품 상세 조회 테스트")
    @Transactional
    void productDetailTest() {
        // given
        Product mockProduct = createProductMock();
        ProductDto.ReqProductDetail reqProductDetail = ProductDto.ReqProductDetail.builder()
                .productId(mockProduct.getId())
                .build();

        // when
        ProductDto.ResProductDetail productDetailDto = productService.getProductDetail(reqProductDetail);
        // then
        Assertions.assertThat(productDetailDto.getProductId()).isEqualTo(mockProduct.getId());
        Assertions.assertThat(productDetailDto.getName()).isEqualTo(mockProduct.getName());
        Assertions.assertThat(productDetailDto.getColor()).isEqualTo(mockProduct.getColor());
        Assertions.assertThat(productDetailDto.getPrice()).isEqualTo(mockProduct.getPrice());
    }
}