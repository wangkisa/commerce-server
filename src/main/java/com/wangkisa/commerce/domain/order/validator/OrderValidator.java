package com.wangkisa.commerce.domain.order.validator;

import com.wangkisa.commerce.domain.order.code.OrderErrorCode;
import com.wangkisa.commerce.domain.order.dto.OrderDTO;
import com.wangkisa.commerce.domain.order.entity.Order;
import com.wangkisa.commerce.domain.product.code.ProductErrorCode;
import com.wangkisa.commerce.domain.product.entity.Product;
import com.wangkisa.commerce.domain.product.repository.ProductRepository;
import com.wangkisa.commerce.domain.user.code.UserErrorCode;
import com.wangkisa.commerce.domain.user.entity.User;
import com.wangkisa.commerce.domain.user.repository.UserRepository;
import com.wangkisa.commerce.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class OrderValidator {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // 전체 요청의 상품별 합계 요청 수량이 상품의 수량 이하인지 체크
    @Transactional(readOnly = true)
    public void checkTotalQuantity(OrderDTO.ReqRegisterOrder reqRegisterOrder) {

        reqRegisterOrder.getOrderProductList().stream()
                .collect(Collectors.toMap(orderProduct -> orderProduct.getProductId(),
                        orderProduct -> orderProduct.getProductQuantity(),
                        Integer::sum))
                .forEach((productId, quantity) -> {
                    Product product = getProductById( productId);
                    product.checkQuantity(quantity);
                });
    }

    @Transactional(readOnly = true)
    private Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new CustomException(ProductErrorCode.ERROR_NOT_FOUND_PRODUCT));
    }

    @Transactional(readOnly = true)
    public void checkTotalPrice(Order order, Long userId) {
        User user = getUserById(userId);
        BigDecimal orderTotalPrice = order.getOrderProducts().stream()
                .map(orderProduct -> orderProduct.getTotalPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (orderTotalPrice.compareTo(user.getPoint()) > 0) {
            throw new CustomException(OrderErrorCode.ERROR_LACK_OF_USER_POINT);
        }
    }

    @Transactional(readOnly = true)
    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new CustomException(UserErrorCode.ERROR_NOT_FOUND_USER));
    }
}
