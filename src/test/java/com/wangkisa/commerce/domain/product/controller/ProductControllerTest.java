package com.wangkisa.commerce.domain.product.controller;

import com.wangkisa.commerce.domain.common.code.StatusCode;
import com.wangkisa.commerce.domain.jwt.JwtTokenProvider;
import com.wangkisa.commerce.domain.product.entity.Product;
import com.wangkisa.commerce.domain.product.repository.ProductRepository;
import com.wangkisa.commerce.domain.product.service.ProductService;
import com.wangkisa.commerce.domain.user.dto.UserDto;
import com.wangkisa.commerce.domain.user.service.UserService;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    private static final String BASE_URL = "/api/product";
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserService userService;

    @Transactional
    private Product createProductMock() {
        return productRepository.save(Product.builder()
                .name("테스트 상품")
                .color("red")
                .quantity(2)
                .price(BigDecimal.valueOf(2000))
                .build());
    }
    @Transactional
    private UserDto.ResUserInfo createUserMock() {
        String email = "test@test.com";
        String nickName = "테스트@@";
        String phone = "010-1234-5678";
        UserDto.ReqSignUp signUpRequest = UserDto.ReqSignUp.builder()
                .email(email)
                .nickName(nickName)
                .password("testtest")
                .phone(phone)
                .build();
        return userService.signUp(signUpRequest);
    }

    @Test
    @DisplayName("상품 목록 조회 API 테스트")
    @Transactional
    void getProductListTest() throws Exception {
        // given
        UserDto.ResUserInfo userMock = createUserMock();
        Product productMock = createProductMock();

        // when
        // then
        String accessToken = jwtTokenProvider.createToken(userMock.getEmail()).getAccessToken();
        mockMvc.perform(post(BASE_URL + "/getProductList")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(StatusCode.OK_CODE))
                .andExpect(jsonPath("$.message").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.productList[0].productId").value(productMock.getId()))
                .andExpect(jsonPath("$.data.productList[0].name").value(productMock.getName()))
                .andExpect(jsonPath("$.data.productList[0].price").value(productMock.getPrice()))
                ;
    }
}
