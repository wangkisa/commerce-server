package com.wangkisa.commerce.domain.product.code;

import com.wangkisa.commerce.exception.ErrorCodeInterface;

public enum ProductErrorCode implements ErrorCodeInterface {

    ERROR_NOT_FOUND_PRODUCT(8001, "찾을 수 없는 상품 정보입니다."),
    ;

    private final int code;
    private final String msg;

    ProductErrorCode(int code, String msg) {
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