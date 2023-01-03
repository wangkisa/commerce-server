package com.wangkisa.commerce.domain.order.service;

import com.wangkisa.commerce.domain.order.code.OrderErrorCode;
import com.wangkisa.commerce.domain.order.dto.OrderDTO;
import com.wangkisa.commerce.domain.order.entity.DeliveryInfo;
import com.wangkisa.commerce.domain.order.entity.Order;
import com.wangkisa.commerce.domain.order.entity.OrderProduct;
import com.wangkisa.commerce.domain.order.repository.OrderRepository;
import com.wangkisa.commerce.domain.order.validator.OrderValidator;
import com.wangkisa.commerce.domain.product.code.ProductErrorCode;
import com.wangkisa.commerce.domain.product.entity.Product;
import com.wangkisa.commerce.domain.product.repository.ProductRepository;
import com.wangkisa.commerce.domain.user.code.UserErrorCode;
import com.wangkisa.commerce.domain.user.entity.User;
import com.wangkisa.commerce.domain.user.repository.UserRepository;
import com.wangkisa.commerce.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OrderDTO.ResOrderInfo registerOrder(OrderDTO.ReqRegisterOrder reqRegisterOrder, Long userId) {

        OrderValidator orderValidator = new OrderValidator(productRepository, userRepository);
        orderValidator.checkTotalQuantity(reqRegisterOrder);

        User findUser = getUserFindById(userId);

        DeliveryInfo deliveryInfo = DeliveryInfo.builder()
                .receiverName(reqRegisterOrder.getReceiverName())
                .receiverAddress(reqRegisterOrder.getReceiverAddress())
                .etcMessage(reqRegisterOrder.getEtcMessage())
                .build();
        Order order = Order.builder()
                .user(findUser)
                .deliveryInfo(deliveryInfo)
                .build();
        Order savedOrder = orderRepository.save(order);

        List<OrderDTO.OrderProductInfo> orderProductInfoList = reqRegisterOrder.getOrderProductList().stream().map(reqProduct -> {
                    Product product = getProductFindById(reqProduct.getProductId());
                    OrderProduct savedOrderProduct = savedOrder.addOrderProduct(product, reqProduct.getProductQuantity());
                    return OrderDTO.OrderProductInfo.fromOrderProduct(savedOrderProduct);
                }
        ).collect(Collectors.toList());
        return OrderDTO.ResOrderInfo.fromOrder(savedOrder, orderProductInfoList);
    }

    @Override
    public OrderDTO.ResOrderInfo purchaseOrder(OrderDTO.ReqPurchaseOrder reqPurchaseOrder, Long userId) {

        OrderValidator orderValidator = new OrderValidator(productRepository, userRepository);
        Order order = getOrderFindById(reqPurchaseOrder.getOrderId());
        orderValidator.checkTotalPrice(order, userId);

        return null;
    }

    @Transactional(readOnly = true)
    private Order getOrderFindById(String orderId) {
        return orderRepository.findByUUID(UUID.fromString(orderId)).orElseThrow(() -> new CustomException(OrderErrorCode.ERROR_NOT_FOUND_ORDER));
    }

    @Transactional(readOnly = true)
    private User getUserFindById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new CustomException(UserErrorCode.ERROR_NOT_FOUND_USER_INFO));
    }

    @Transactional(readOnly = true)
    private Product getProductFindById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new CustomException(ProductErrorCode.ERROR_NOT_FOUND_PRODUCT));
    }
}
