package com.wangkisa.commerce.domain.order.service;

import com.wangkisa.commerce.domain.product.entity.Product;
import com.wangkisa.commerce.domain.product.repository.ProductRepository;
import com.wangkisa.commerce.domain.product.service.ProductService;
import com.wangkisa.commerce.domain.user.dto.UserDto;
import com.wangkisa.commerce.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceImplTest {

    @Autowired
    UserService userService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Autowired
    OrderService orderService;
    private UserDto.ResUserInfo defaultUser;
    private Product defaultProduct;

    private UserDto.ReqSignUp reqSignUpUserDto() {
        return UserDto.ReqSignUp.builder()
                .email("test@test.com")
                .nickName("테스트 닉네임")
                .phone("010-1234-5678")
                .password("testtest")
                .build();
    }

    @BeforeEach
    void setUp() {
        defaultUser = signUpUser();
        defaultProduct = createProduct();
    }

    @Transactional
    private Product createProduct() {
        return productRepository.save(Product.builder()
                .name("테스트 상품")
                .color("red")
                .quantity(4)
                .price(BigDecimal.valueOf(3000))
                .build());
    }

    @Transactional
    private UserDto.ResUserInfo signUpUser() {
        return userService.signUp(reqSignUpUserDto());
    }

    @Test
    @DisplayName("주문 등록 후 주문서 출력 테스트")
    @Transactional
    void registerOrderTest() {
        // given
        // 회원 가입, 상품 등록,

        // when
        // 주문 등록
        orderService.registerOrder();
        // then
    }

}