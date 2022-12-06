package com.wangkisa.commerce.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangkisa.commerce.domain.common.code.StatusCode;
import com.wangkisa.commerce.domain.user.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    private static final String BASE_URL = "/api/user";


    @Test
    @DisplayName("회원가입 성공 테스트")
    @Transactional
    void signUpTest() {
        //given
        String title = "Test title";
        String content = "Test content";
        String author = "gorany";
        //when
        String body = null;
        try {
            body = mapper.writeValueAsString(
                    UserDto.ReqSignUp.builder()
                            .email("test@test.com")
                            .nickName("테스트 닉네임")
                            .phone("010-1234-5678")
                            .password("testtest")
                            .build()
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        //then
        try {
            mockMvc.perform(post(BASE_URL + "/signUp")
                            .content(body) //HTTP Body에 데이터를 담는다
                            .contentType(MediaType.APPLICATION_JSON) //보내는 데이터의 타입을 명시
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().string("1"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}