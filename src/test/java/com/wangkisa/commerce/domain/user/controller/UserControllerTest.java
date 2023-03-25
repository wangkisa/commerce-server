package com.wangkisa.commerce.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangkisa.commerce.common.AcceptanceTest;
import com.wangkisa.commerce.configuration.TestConfig;
import com.wangkisa.commerce.domain.common.code.StatusCode;
import com.wangkisa.commerce.domain.user.dto.UserDTO;
import com.wangkisa.commerce.domain.user.service.UserService;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends AcceptanceTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private UserService userService;

    private static final String BASE_URL = "/api/user";


    @Test
    @DisplayName("회원가입 성공 테스트")
    void signUpTest() throws Exception {
        //given
        String email = "test@test.com";
        String nickName = "테스트@@";
        String phone = "010-1234-5678";
        UserDTO.ReqSignUp signUpRequest = UserDTO.ReqSignUp.builder()
                .email(email)
                .nickName(nickName)
                .phone(phone)
                .password("testtest")
                .build();

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(signUpRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(BASE_URL + "/signUp")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getInt("code")).isEqualTo(StatusCode.OK_CODE);
        Map<String, Object> data = response.jsonPath().getMap("data");

        assertThat(data.get("email")).isEqualTo(signUpRequest.getEmail());
        assertThat(data.get("nickname")).isEqualTo(signUpRequest.getNickName());
        assertThat(data.get("phone")).isEqualTo(signUpRequest.getPhone());
    }

    @Test
    @DisplayName("회원로그인 성공 테스트")
    void signInTest() throws Exception {
        // given
        String email = "test@test.com";
        String nickName = "테스트@@";
        String phone = "010-1234-5678";
        UserDTO.ReqSignIn signInRequest = UserDTO.ReqSignIn.builder()
                .email(email)
                .password("testtest")
                .build();

        UserDTO.ReqSignUp signUpRequest = UserDTO.ReqSignUp.builder()
                .email(signInRequest.getEmail())
                .nickName(nickName)
                .password(signInRequest.getPassword())
                .phone(phone)
                .build();

        UserDTO.ResUserInfo resUserInfo = userService.signUp(signUpRequest);

        System.out.println("resUserInfo = " + resUserInfo);

        // when
        // then
//        mockMvc.perform(post(BASE_URL + "/signIn")
//                        .content(mapper.writeValueAsString(signInRequest))
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(StatusCode.OK_CODE))
//                .andExpect(jsonPath("$.message").value(IsNull.nullValue()))
//                .andExpect(jsonPath("$.data.email").value(email))
//                .andExpect(jsonPath("$.data.nickname").value(nickName))
//                .andExpect(jsonPath("$.data.phone").value(phone))
//                .andExpect(jsonPath("$.data.accessToken").value(IsNull.notNullValue()))
//                .andExpect(jsonPath("$.data.refreshToken").value(IsNull.notNullValue()))
//        ;
    }
}