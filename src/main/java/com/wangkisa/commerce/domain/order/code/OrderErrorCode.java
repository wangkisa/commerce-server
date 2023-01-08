package com.wangkisa.commerce.domain.order.code;

import com.wangkisa.commerce.exception.ErrorCodeInterface;

public enum OrderErrorCode implements ErrorCodeInterface {

    ERROR_NOT_FOUND_ORDER(9000, "찾을 수 없는 주문번호입니다."),
    ERROR_INVALID_ORDER_STATUS(9001, "잘못된 주문의 상태값입니다."),
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
