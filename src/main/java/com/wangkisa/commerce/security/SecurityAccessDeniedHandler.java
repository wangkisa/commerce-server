package com.wangkisa.commerce.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangkisa.commerce.domain.common.response.ApiResponse;
import com.wangkisa.commerce.domain.common.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
@Component
public class SecurityAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public SecurityAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        log.error("SecurityAccessDenied : {} | {}", request.getRequestURI(), e.getMessage());
        ResponseUtil.writeJson(response, objectMapper, new ApiResponse(FORBIDDEN.value(), "해당 리소스에 접근하실 수 없습니다.", Arrays.asList(e.getMessage()), null));
    }
}
