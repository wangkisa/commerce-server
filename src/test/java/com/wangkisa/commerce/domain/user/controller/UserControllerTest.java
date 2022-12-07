package com.wangkisa.commerce.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangkisa.commerce.domain.common.code.StatusCode;
import com.wangkisa.commerce.domain.jwt.JwtTokenProvider;
import com.wangkisa.commerce.domain.user.dto.UserDto;
import com.wangkisa.commerce.domain.user.repository.UserRepository;
import com.wangkisa.commerce.domain.user.service.UserService;
import com.wangkisa.commerce.security.SecurityAccessDeniedHandler;
import com.wangkisa.commerce.security.SecurityAuthenticationEntryPoint;
import com.wangkisa.commerce.security.SecurityAuthenticationFilter;
import com.wangkisa.commerce.security.SecurityUserDetailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

//@WebMvcTest(UserController.class)
//@SpringBootTest
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;


    private static final String BASE_URL = "/api/user";


    @Test
    @DisplayName("회원가입 성공 테스트")
    @Transactional
    void signUpTest() throws Exception {
        //given
        String title = "Test title";
        String content = "Test content";
        String author = "gorany";
        UserDto.ReqSignUp signUpRequest = UserDto.ReqSignUp.builder()
                .email("test@test.com")
                .nickName("테스트@@")
                .phone("010-1234-5678")
                .password("testtest")
                .build();
        //when
//        doNothing().when(userService).signUp(signUpRequest);

//        System.out.println("signUpRequest = " + signUpRequest);
        System.out.println("signUpRequest = end");
        //then
        mockMvc.perform(post("/api/user/signUp")
                        .content(mapper.writeValueAsString(signUpRequest)) //HTTP Body에 데이터를 담는다
                        .contentType(MediaType.APPLICATION_JSON) //보내는 데이터의 타입을 명시
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}