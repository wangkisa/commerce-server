package com.wangkisa.commerce.domain.order.service;

import com.wangkisa.commerce.domain.order.dto.OrderDTO;
import com.wangkisa.commerce.domain.product.entity.Product;
import com.wangkisa.commerce.domain.product.repository.ProductRepository;
import com.wangkisa.commerce.domain.product.service.ProductService;
import com.wangkisa.commerce.domain.user.dto.UserDTO;
import com.wangkisa.commerce.domain.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    private UserDTO.ResUserInfo defaultUser;
    private Product defaultProduct;

    private UserDTO.ReqSignUp reqSignUpUserDto() {
        return UserDTO.ReqSignUp.builder()
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
    private UserDTO.ResUserInfo signUpUser() {
        return userService.signUp(reqSignUpUserDto());
    }

    @Test
    @DisplayName("주문 등록 후 주문서 출력 테스트")
    @Transactional
    void registerOrderTest() {
        // given
        OrderDTO.ReqRegisterOrder reqRegisterOrder = getReqRegisterOrder();

        // when
        // 주문 등록
        OrderDTO.ResOrderInfo resOrderInfo = orderService.registerOrder(reqRegisterOrder, defaultUser.getUserId());

        // then
        Assertions.assertThat(resOrderInfo.getReceiverName()).isEqualTo(resOrderInfo.getReceiverName());
        Assertions.assertThat(resOrderInfo.getReceiverAddress()).isEqualTo(resOrderInfo.getReceiverAddress());
    }

    private OrderDTO.ReqRegisterOrder getReqRegisterOrder() {
        OrderDTO.RegisterOrderProduct reqOrderProduct = OrderDTO.RegisterOrderProduct.builder()
                .productId(defaultProduct.getId())
                .productQuantity(2)
                .productName(defaultProduct.getName())
                .productPrice(defaultProduct.getPrice().longValue())
                .build();
        List<OrderDTO.RegisterOrderProduct> orderProductList = new ArrayList<>();
        orderProductList.add(reqOrderProduct);
        OrderDTO.ReqRegisterOrder reqRegisterOrder = OrderDTO.ReqRegisterOrder.builder()
                .receiverName("아무개")
                .receiverAddress("구리시")
                .etcMessage("배송 빨리")
                .orderProductList(orderProductList)
                .build();
        return reqRegisterOrder;
    }

}