package com.wangkisa.commerce.domain.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangkisa.commerce.domain.common.response.ApiResponse;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtil {

    public static void writeJson(HttpServletResponse response, ObjectMapper objectMapper, ApiResponse errorResponse) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
