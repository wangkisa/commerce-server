package com.wangkisa.commerce.domain.common.util;

import java.math.BigDecimal;

public class BigDecimalUtil {


    public static BigDecimal multiply(BigDecimal front, Integer back) {
        return front.multiply(new BigDecimal(back));
    }

}
