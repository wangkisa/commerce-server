package com.wangkisa.commerce.domain.user.controller;

import com.wangkisa.commerce.domain.common.response.ApiResponse;
import com.wangkisa.commerce.domain.user.dto.UserDto;
import com.wangkisa.commerce.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원 가입")
    @PostMapping(value = "/signUp", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<UserDto.ResUserInfo> signUp(@Validated @RequestBody UserDto.ReqSignUp requestDto) {
        return ApiResponse.success(userService.signUp(requestDto));
    }

    @Operation(summary = "회원 로그인")
    @PostMapping(value = "/signIn", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<UserDto.ResUserInfo> signIn(@Validated @RequestBody UserDto.ReqSignIn requestDto) {
        return ApiResponse.success(userService.signIn(requestDto));
    }
}
