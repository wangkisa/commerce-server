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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    private final OrderValidator orderValidator;

    @Override
    @Transactional
    public OrderDTO.ResOrderInfo registerOrder(OrderDTO.ReqRegisterOrder reqRegisterOrder, Long userId) {

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

    /**
     * 주문번호를 토대로 구매 진행
     */
    @Override
    @Transactional
    public OrderDTO.ResOrderInfo purchaseOrder(OrderDTO.ReqPurchaseOrder reqPurchaseOrder, Long userId) {

        Order order = getOrderFindById(reqPurchaseOrder.getOrderId());
        List<OrderDTO.RegisterOrderProduct> orderProductList = new ArrayList<>();
        order.getOrderProducts().forEach(orderProduct -> {
            orderProductList.add(OrderDTO.RegisterOrderProduct.builder()
                            .productId(orderProduct.getProduct().getId())
                            .productQuantity(orderProduct.getProductQuantity())
                            .build()
            );
        });

        OrderDTO.ReqRegisterOrder reqRegisterOrder = OrderDTO.ReqRegisterOrder.builder()
                .orderProductList(orderProductList)
                .build();
        orderValidator.checkTotalQuantity(reqRegisterOrder);
        orderValidator.checkTotalPrice(order, userId);
        User user = getUserFindById(userId);

        user.subtractPoint(order.getOrderTotalPrice());

        return OrderDTO.ResOrderInfo.fromOrder(order, null);
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
