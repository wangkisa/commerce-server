package com.wangkisa.commerce.domain.order.service;

import com.wangkisa.commerce.domain.product.service.ProductService;
import com.wangkisa.commerce.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceImplTest {

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    OrderService orderService;

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