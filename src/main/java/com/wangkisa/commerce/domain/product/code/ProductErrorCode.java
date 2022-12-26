package com.wangkisa.commerce.domain.product.code;

import com.wangkisa.commerce.exception.ErrorCodeInterface;

public enum ProductErrorCode implements ErrorCodeInterface {

    ERROR_NOT_FOUND_PRODUCT(8001, "찾을 수 없는 상품 정보입니다."),
    ERROR_LACK_OF_PRODUCT_QUANTITY(8002, "해당 상품의 재고가 부족합니다."),
    ERROR_NONE_OF_PRODUCT_QUANTITY(8003, "해당 상품은 매진 되었습니다."),
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
