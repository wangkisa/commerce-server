package com.wangkisa.commerce.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangkisa.commerce.domain.common.response.ApiResponse;
import com.wangkisa.commerce.domain.common.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;


@Slf4j
@Component
public class SecurityAuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public SecurityAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        log.error("UNAUTHORIZED : {} | {}", request.getRequestURI(), e.getMessage());
        ResponseUtil.writeJson(response, objectMapper,
                new ApiResponse(UNAUTHORIZED.value(), "올바르지 못한 인증입니다.", Arrays.asList(e.getMessage()), null)
        );
    }
}
