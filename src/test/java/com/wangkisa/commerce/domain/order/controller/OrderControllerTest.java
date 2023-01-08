package com.wangkisa.commerce.domain.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangkisa.commerce.configuration.TestConfig;
import com.wangkisa.commerce.domain.common.code.StatusCode;
import com.wangkisa.commerce.domain.jwt.JwtTokenProvider;
import com.wangkisa.commerce.domain.order.dto.OrderDTO;
import com.wangkisa.commerce.domain.order.service.OrderService;
import com.wangkisa.commerce.domain.product.entity.Product;
import com.wangkisa.commerce.domain.product.repository.ProductRepository;
import com.wangkisa.commerce.domain.user.dto.UserDTO;
import com.wangkisa.commerce.domain.user.entity.User;
import com.wangkisa.commerce.domain.user.repository.UserRepository;
import com.wangkisa.commerce.domain.user.service.UserService;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(TestConfig.class)
@AutoConfigureMockMvc
@Transactional
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    private UserDTO.ResUserInfo defaultUser;
    private Product defaultProduct;
    private String accessToken;

    private static final String BASE_URL = "/api/order";

    @Autowired
    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        defaultUser = createUserMock();
        defaultProduct = createProductMock();
        accessToken = "Bearer " + jwtTokenProvider.createToken(defaultUser.getEmail()).getAccessToken();
    }

    private Product createProductMock() {
        return productRepository.save(Product.builder()
                .name("테스트 상품")
                .color("red")
                .quantity(2)
                .price(BigDecimal.valueOf(2000))
                .build());
    }

    private UserDTO.ResUserInfo createUserMock() {
        String email = "test@test.com";
        String nickName = "테스트@@";
        String phone = "010-1234-5678";
        UserDTO.ReqSignUp signUpRequest = UserDTO.ReqSignUp.builder()
                .email(email)
                .nickName(nickName)
                .password("testtest")
                .phone(phone)
                .build();
        return userService.signUp(signUpRequest);
    }

    @Test
    @DisplayName("주문서 등록 API 테스트")
    void registerOrderTest() throws Exception {
        // given
        OrderDTO.RegisterOrderProduct reqOrderProduct = OrderDTO.RegisterOrderProduct.builder()
                .productId(defaultProduct.getId())
                .productQuantity(2)
                .productName(defaultProduct.getName())
                .productPrice(defaultProduct.getPrice().longValue())
                .build();
        List<OrderDTO.RegisterOrderProduct> orderProductList = new ArrayList<>();
        orderProductList.add(reqOrderProduct);
        OrderDTO.ReqRegisterOrder reqRegisterOrder = OrderDTO.ReqRegisterOrder.builder()
                .receiverName("test 유저")
                .receiverAddress("구리시")
                .etcMessage("배송 빨리")
                .orderProductList(orderProductList)
                .build();

        // when
        // then
        mockMvc.perform(post(BASE_URL + "/registerOrder")
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .content(mapper.writeValueAsString(reqRegisterOrder))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(StatusCode.OK_CODE))
                .andExpect(jsonPath("$.message").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.orderId").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.data.receiverName").value(reqRegisterOrder.getReceiverName()))
                .andExpect(jsonPath("$.data.receiverAddress").value(reqRegisterOrder.getReceiverAddress()))
                .andExpect(jsonPath("$.data.orderProductList[0].productId").value(defaultProduct.getId()))
                .andExpect(jsonPath("$.data.orderProductList[0].productQuantity").value(2))
                .andExpect(jsonPath("$.data.orderProductList[0].productName").value(defaultProduct.getName()))
                .andExpect(jsonPath("$.data.orderProductList[0].productPrice").value(defaultProduct.getPrice()))
                .andExpect(jsonPath("$.data.orderProductList[0].totalPrice").value(defaultProduct.getPrice().longValue()*2));
    }

    @Test
    @DisplayName("상품 구매 진행 API 테스트")
    void purchaseOrderTest() throws Exception {
        // given
        OrderDTO.RegisterOrderProduct reqOrderProduct = OrderDTO.RegisterOrderProduct.builder()
                .productId(defaultProduct.getId())
                .productQuantity(2)
                .productName(defaultProduct.getName())
                .productPrice(defaultProduct.getPrice().longValue())
                .build();
        List<OrderDTO.RegisterOrderProduct> orderProductList = new ArrayList<>();
        orderProductList.add(reqOrderProduct);
        OrderDTO.ReqRegisterOrder reqRegisterOrder = OrderDTO.ReqRegisterOrder.builder()
                .receiverName("test 유저")
                .receiverAddress("구리시")
                .etcMessage("배송 빨리")
                .orderProductList(orderProductList)
                .build();

        User user = userRepository.findById(defaultUser.getUserId()).get();
        // 7000 포인트 충전
        user.chargePoint(BigDecimal.valueOf(7000L));

        OrderDTO.ResOrderInfo resOrderInfo = orderService.registerOrder(reqRegisterOrder, defaultUser.getUserId());
        OrderDTO.ReqPurchaseOrder reqPurchaseOrder = OrderDTO.ReqPurchaseOrder.builder()
                .orderId(resOrderInfo.getOrderId())
                .build();

        // when
        // then
        mockMvc.perform(post(BASE_URL + "/purchaseOrder")
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .content(mapper.writeValueAsString(reqPurchaseOrder))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(StatusCode.OK_CODE))
                .andExpect(jsonPath("$.message").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.orderId").value(resOrderInfo.getOrderId()));
    }
}