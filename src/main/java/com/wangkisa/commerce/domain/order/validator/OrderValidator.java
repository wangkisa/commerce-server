package com.wangkisa.commerce.domain.order.validator;

import com.wangkisa.commerce.domain.order.dto.OrderDTO;
import com.wangkisa.commerce.domain.product.code.ProductErrorCode;
import com.wangkisa.commerce.domain.product.entity.Product;
import com.wangkisa.commerce.domain.product.repository.ProductRepository;
import com.wangkisa.commerce.exception.CustomException;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

public class OrderValidator {

    // 전체 요청의 상품별 합계 요청 수량이 상품의 수량 이하인지 체크
    @Transactional(readOnly = true)
    public static void checkTotalQuantity(OrderDTO.ReqRegisterOrder reqRegisterOrder, ProductRepository productRepository) {

        reqRegisterOrder.getOrderProductList().stream()
                .collect(Collectors.toMap(orderProduct -> orderProduct.getProductId(),
                        orderProduct -> orderProduct.getProductQuantity(),
                        Integer::sum))
                .forEach((productId, quantity) -> {
                    Product product = productRepository.findById(productId).orElseThrow(() -> new CustomException(ProductErrorCode.ERROR_NOT_FOUND_PRODUCT));
                    product.checkQuantity(quantity);
                });
    }
}
