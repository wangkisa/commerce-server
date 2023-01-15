package com.wangkisa.commerce.domain.product.service;

import com.wangkisa.commerce.configuration.TestConfig;
import com.wangkisa.commerce.domain.product.code.ProductErrorCode;
import com.wangkisa.commerce.domain.product.dto.PageRequestDTO;
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
        // 상품을 11개 등록
        final String prefixProductName = "apple";
        final int multiplyPriceValue = 1000;
        final int maxRequestNum = 11;
        for (int i = 1; i <= maxRequestNum; i++){
            createProduct(prefixProductName + i, i, Long.valueOf(i * multiplyPriceValue));
        }

        PageRequestDTO pageRequestDTO = PageRequestDTO.of(0, 10);

        // when
        ProductDTO.ResProductList productList = productService.getProductList(pageRequestDTO);
        // then
        // pagination 으로 10개 등록된 상품 내용 조회 확인
        Assertions.assertThat(productList.getProductList().size()).isEqualTo(10);
        Assertions.assertThat(productList.getTotalCount()).isEqualTo(maxRequestNum);
        Assertions.assertThat(productList.isHasNext()).isEqualTo(true);

        for (int i = 0; i < 10; i++){
            // 상품 조회 결과가 최신순으로 등록한 순서가 역순으로 나오기 때문에 아래 같은 계산걊으로 사용
            int reverseIndex = maxRequestNum - i;
            Assertions.assertThat(productList.getProductList().get(i).getName()).isEqualTo(prefixProductName + reverseIndex);
            Assertions.assertThat(productList.getProductList().get(i).getQuantity()).isEqualTo(reverseIndex);
            Assertions.assertThat(productList.getProductList().get(i).getPrice()).isEqualTo(BigDecimal.valueOf(reverseIndex * multiplyPriceValue));
        }

    }
    private Product createProduct(String productName, Integer quantity, Long price) {
        return productRepository.save(Product.builder()
                .name(productName)
                .color("red")
                .quantity(quantity)
                .price(BigDecimal.valueOf(price))
                .build());
    }

    @Test
    @DisplayName("상품 상세 조회 알수없는 상품아이디로 실패 테스트")
    void productDetailNotFoundTest() {
        // given
        Product mockProduct = createProduct("apple", 1, 1000L);
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
        Product product = createProduct("apple", 1, 1000L);
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