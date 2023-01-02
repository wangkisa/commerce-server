package com.wangkisa.commerce.domain.order.code;

import com.wangkisa.commerce.exception.ErrorCodeInterface;

public enum OrderErrorCode implements ErrorCodeInterface {

    ERROR_NOT_FOUND_USER(9001, "찾을 수 없는 유저입니다."),
    ERROR_LACK_OF_USER_POINT(9002, "유저의 보유 포인트가 부족합니다."),
    ;

    private final int code;
    private final String msg;

    OrderErrorCode(int code, String msg) {
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
