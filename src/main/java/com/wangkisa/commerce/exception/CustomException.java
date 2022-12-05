package com.wangkisa.commerce.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class CustomException extends RuntimeException {

    public final int code;
    public final String message;
    public final List<String> errorDetails;

    public CustomException(int code, String message, List<String> errorDetails) {
        this.code = code;
        this.message = message;
        this.errorDetails = errorDetails;
    }

    public CustomException(ErrorCodeInterface errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMsg();
        this.errorDetails = null;
    }
}
