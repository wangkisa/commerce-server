package com.wangkisa.commerce.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangkisa.commerce.domain.common.code.StatusCode;
import com.wangkisa.commerce.domain.user.dto.UserDTO;
import com.wangkisa.commerce.domain.user.service.UserService;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private UserService userService;

    private static final String BASE_URL = "/api/user";


    @Test
    @DisplayName("회원가입 성공 테스트")
    @Transactional
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
        //then
        mockMvc.perform(post(BASE_URL + "/signUp")
                        .content(mapper.writeValueAsString(signUpRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(StatusCode.OK_CODE))
                .andExpect(jsonPath("$.message").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.email").value(email))
                .andExpect(jsonPath("$.data.nickname").value(nickName))
                .andExpect(jsonPath("$.data.phone").value(phone));
    }

    @Test
    @DisplayName("회원로그인 성공 테스트")
    @Transactional
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

        userService.signUp(signUpRequest);

        // when
        // then
        mockMvc.perform(post(BASE_URL + "/signIn")
                        .content(mapper.writeValueAsString(signInRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(StatusCode.OK_CODE))
                .andExpect(jsonPath("$.message").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.email").value(email))
                .andExpect(jsonPath("$.data.nickname").value(nickName))
                .andExpect(jsonPath("$.data.phone").value(phone))
                .andExpect(jsonPath("$.data.accessToken").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.data.refreshToken").value(IsNull.notNullValue()))
        ;
    }
}