package com.wangkisa.commerce.domain.product.service;

import com.wangkisa.commerce.configuration.TestConfig;
import com.wangkisa.commerce.domain.product.code.ProductErrorCode;
import com.wangkisa.commerce.domain.product.dto.ProductDTO;
import com.wangkisa.commerce.domain.product.entity.Product;
import com.wangkisa.commerce.domain.product.repository.ProductRepository;
import com.wangkisa.commerce.exception.CustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Import(TestConfig.class)
@Transactional
class ProductServiceTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Test
    @DisplayName("상품 목록 조회 테스트")
    void productListTest() {
        // given
        Product mockProduct = createProduct();
        // when
        ProductDTO.ResProductList productList = productService.getProductList();
        // then
        Assertions.assertThat(productList.getProductList().size()).isEqualTo(1);
        Assertions.assertThat(productList.getProductList().get(0).getName()).isEqualTo(mockProduct.getName());
        Assertions.assertThat(productList.getProductList().get(0).getPrice()).isEqualTo(mockProduct.getPrice());
    }
    private Product createProduct() {
        return productRepository.save(Product.builder()
                .name("테스트 상품")
                .color("red")
                .quantity(2)
                .price(BigDecimal.valueOf(2000))
                .build());
    }

    @Test
    @DisplayName("상품 상세 조회 알수없는 상품아이디로 실패 테스트")
    void productDetailNotFoundTest() {
        // given
        Product mockProduct = createProduct();
        // 상품 아이디에 1을 더해서 엉뚱한 값으로 할당
        ProductDTO.ReqProductDetail reqProductDetail = ProductDTO.ReqProductDetail.builder()
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
    void productDetailTest() {
        // given
        Product product = createProduct();
        ProductDTO.ReqProductDetail reqProductDetail = ProductDTO.ReqProductDetail.builder()
                .productId(product.getId())
                .build();

        // when
        ProductDTO.ResProductDetail productDetailDto = productService.getProductDetail(reqProductDetail);
        // then
        Assertions.assertThat(productDetailDto.getProductId()).isEqualTo(product.getId());
        Assertions.assertThat(productDetailDto.getName()).isEqualTo(product.getName());
        Assertions.assertThat(productDetailDto.getColor()).isEqualTo(product.getColor());
        Assertions.assertThat(productDetailDto.getPrice()).isEqualTo(product.getPrice());
    }
}