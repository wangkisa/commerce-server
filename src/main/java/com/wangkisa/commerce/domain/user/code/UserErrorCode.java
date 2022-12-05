package com.wangkisa.commerce.domain.user.code;

import com.wangkisa.commerce.exception.ErrorCodeInterface;

public enum UserErrorCode implements ErrorCodeInterface {

    ERROR_SIGNUP_DUPLICATE_EMAIL(7001, "중복으로 존재하는 email입니다."),
    ERROR_SIGNUP_DUPLICATE_NICKNAME(7002, "중복으로 존재하는 닉네임입니다."),
    ;

    private final int code;
    private final String msg;

    UserErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}
