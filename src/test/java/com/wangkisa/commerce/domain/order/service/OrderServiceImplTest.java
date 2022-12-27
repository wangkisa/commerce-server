package com.wangkisa.commerce.domain.order.service;

import com.wangkisa.commerce.domain.order.dto.OrderDTO;
import com.wangkisa.commerce.domain.product.code.ProductErrorCode;
import com.wangkisa.commerce.domain.product.entity.Product;
import com.wangkisa.commerce.domain.product.repository.ProductRepository;
import com.wangkisa.commerce.domain.product.service.ProductService;
import com.wangkisa.commerce.domain.user.dto.UserDTO;
import com.wangkisa.commerce.domain.user.service.UserService;
import com.wangkisa.commerce.exception.CustomException;
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

import static org.junit.jupiter.api.Assertions.assertThrows;

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

    private OrderDTO.ReqRegisterOrder getReqRegisterOrder(List<OrderDTO.RegisterOrderProduct> orderProductList) {
        OrderDTO.ReqRegisterOrder reqRegisterOrder = OrderDTO.ReqRegisterOrder.builder()
                .receiverName("아무개")
                .receiverAddress("구리시")
                .etcMessage("배송 빨리")
                .orderProductList(orderProductList)
                .build();
        return reqRegisterOrder;
    }

    private List<OrderDTO.RegisterOrderProduct> getRegisterOrderProducts() {
        OrderDTO.RegisterOrderProduct reqOrderProduct = OrderDTO.RegisterOrderProduct.builder()
                .productId(defaultProduct.getId())
                .productQuantity(2)
                .productName(defaultProduct.getName())
                .productPrice(defaultProduct.getPrice().longValue())
                .build();
        List<OrderDTO.RegisterOrderProduct> orderProductList = new ArrayList<>();
        orderProductList.add(reqOrderProduct);
        return orderProductList;
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
    @DisplayName("주문 등록시 수량 부족 실패 테스트")
    @Transactional
    void registerOrderQuantityErrorTest() {
        // given
        // 등록된 상품 수량에 1을 더해서 상품수량보다 요청 수량이 많도록 설정
        OrderDTO.RegisterOrderProduct reqOrderProduct = OrderDTO.RegisterOrderProduct.builder()
                .productId(defaultProduct.getId())
                .productQuantity(defaultProduct.getQuantity() + 1)
                .productName(defaultProduct.getName())
                .productPrice(defaultProduct.getPrice().longValue())
                .build();
        List<OrderDTO.RegisterOrderProduct> orderProductList = new ArrayList<>();
        orderProductList.add(reqOrderProduct);
        OrderDTO.ReqRegisterOrder reqRegisterOrder = getReqRegisterOrder(orderProductList);

        // when
        // 주문 등록
        CustomException customException = assertThrows(CustomException.class, () -> orderService.registerOrder(reqRegisterOrder, defaultUser.getUserId()));

        // then
        Assertions.assertThat(customException.getCode()).isEqualTo(ProductErrorCode.ERROR_LACK_OF_PRODUCT_QUANTITY.getCode());
        Assertions.assertThat(customException.getMessage()).isEqualTo(ProductErrorCode.ERROR_LACK_OF_PRODUCT_QUANTITY.getMsg());
    }

    @Test
    @DisplayName("주문 등록시 요청 수량을 여러번 호출해서 합계한 총 수량이 부족 실패 테스트")
    @Transactional
    void registerOrderQuantityErrorTest2() {
        // given
        // 등록된 상품 수량에 1을 빼서 상품수량보다 작지만
        // 상품 수량은 4를 보유하지만 두번에 걸쳐 호출해서 요청수량은 3 + 3 이므로 수량 부족 실패
        OrderDTO.RegisterOrderProduct reqOrderProduct = OrderDTO.RegisterOrderProduct.builder()
                .productId(defaultProduct.getId())
                .productQuantity(defaultProduct.getQuantity() - 1)
                .productName(defaultProduct.getName())
                .productPrice(defaultProduct.getPrice().longValue())
                .build();
        List<OrderDTO.RegisterOrderProduct> orderProductList = new ArrayList<>();
        // 요청 수량 3 첫번째 설정
        orderProductList.add(reqOrderProduct);
        // 요청 수량 3 두번째 설정
        orderProductList.add(reqOrderProduct);
        OrderDTO.ReqRegisterOrder reqRegisterOrder = getReqRegisterOrder(orderProductList);

        // when
        // 주문 등록
        CustomException customException = assertThrows(CustomException.class, () -> orderService.registerOrder(reqRegisterOrder, defaultUser.getUserId()));

        // then
        Assertions.assertThat(customException.getCode()).isEqualTo(ProductErrorCode.ERROR_LACK_OF_PRODUCT_QUANTITY.getCode());
        Assertions.assertThat(customException.getMessage()).isEqualTo(ProductErrorCode.ERROR_LACK_OF_PRODUCT_QUANTITY.getMsg());
    }

    @Test
    @DisplayName("주문 등록시 상품 수량이 0인 경우 실패 테스트")
    @Transactional
    void registerOrderZeroQuantityErrorTest() {
        // given
        // 등록된 상품 수량이 0 으로 설정
        Product savedProduct = productRepository.save(Product.builder()
                .name("테스트 상품2")
                .color("blue")
                .quantity(0)
                .price(BigDecimal.valueOf(3000))
                .build());

        OrderDTO.RegisterOrderProduct reqOrderProduct = OrderDTO.RegisterOrderProduct.builder()
                .productId(savedProduct.getId())
                .productQuantity(1)
                .productName(savedProduct.getName())
                .productPrice(savedProduct.getPrice().longValue())
                .build();
        List<OrderDTO.RegisterOrderProduct> orderProductList = new ArrayList<>();
        orderProductList.add(reqOrderProduct);
        OrderDTO.ReqRegisterOrder reqRegisterOrder = getReqRegisterOrder(orderProductList);

        // when
        // 주문 등록
        CustomException customException = assertThrows(CustomException.class, () -> orderService.registerOrder(reqRegisterOrder, defaultUser.getUserId()));

        // then
        Assertions.assertThat(customException.getCode()).isEqualTo(ProductErrorCode.ERROR_NONE_OF_PRODUCT_QUANTITY.getCode());
        Assertions.assertThat(customException.getMessage()).isEqualTo(ProductErrorCode.ERROR_NONE_OF_PRODUCT_QUANTITY.getMsg());
    }

    @Test
    @DisplayName("주문 등록 후 주문서 출력 성공 테스트")
    @Transactional
    void registerOrderTest() {
        // given
        OrderDTO.ReqRegisterOrder reqRegisterOrder = getReqRegisterOrder(getRegisterOrderProducts());

        // when
        // 주문 등록
        OrderDTO.ResOrderInfo resOrderInfo = orderService.registerOrder(reqRegisterOrder, defaultUser.getUserId());
        OrderDTO.OrderProductInfo orderProductInfo = resOrderInfo.getOrderProductList().get(0);

        // then
        Assertions.assertThat(resOrderInfo.getReceiverName()).isEqualTo(resOrderInfo.getReceiverName());
        Assertions.assertThat(resOrderInfo.getReceiverAddress()).isEqualTo(resOrderInfo.getReceiverAddress());
        Assertions.assertThat(resOrderInfo.getEtcMessage()).isEqualTo(resOrderInfo.getEtcMessage());

        Assertions.assertThat(resOrderInfo.getOrderProductList().get(0).getProductId()).isEqualTo(orderProductInfo.getProductId());
        Assertions.assertThat(resOrderInfo.getOrderProductList().get(0).getProductName()).isEqualTo(orderProductInfo.getProductName());
        Assertions.assertThat(resOrderInfo.getOrderProductList().get(0).getProductPrice()).isEqualTo(orderProductInfo.getProductPrice());
        Assertions.assertThat(resOrderInfo.getOrderProductList().get(0).getProductQuantity()).isEqualTo(orderProductInfo.getProductQuantity());

    }

    @Test
    @DisplayName("구매시 보유 포인트가 상품 금액보다 작은 경우 실패 테스트")
    @Transactional
    void $END1() throws Exception {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("정상적인 구매 성공 테스트")
    @Transactional
    void $END2() throws Exception {
        // given

        // when

        // then
    }
}