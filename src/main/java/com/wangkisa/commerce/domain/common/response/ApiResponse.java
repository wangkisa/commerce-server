package com.wangkisa.commerce.domain.common.response;

import com.wangkisa.commerce.domain.common.code.StatusCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@Slf4j
public class ApiResponse<T> {

    private int code;
    private String message;
    private List<String> errorDetails;
    private T data;
    private String responseTime;

    protected ApiResponse() {
    }

    private ApiResponse(int code, String message, List<String> errorDetails, T data) {
        this.code = code;
        this.message = message;
        this.errorDetails = errorDetails;
        this.data = data;
//        this.responseTime = LocalDateTimeUtil.getLocalDateTimeNowString("yyyy-MM-dd hh:mm:ss");
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(StatusCode.OK_CODE, null, null, data);
    }

    public static ApiResponse<Void> successWithNoData() {
        return new ApiResponse<>(StatusCode.OK_CODE, null, null, null);
    }

    // 예외 발생으로 API 에러시 반환
    public static ApiResponse<?> error(int code, String message, List<String> errorDetails) {
        log.error("call ApiResponse.error");
        log.error("code = " + code);
        log.error("message = " + message);
        return new ApiResponse<>(code, message, errorDetails, null);
    }
}
