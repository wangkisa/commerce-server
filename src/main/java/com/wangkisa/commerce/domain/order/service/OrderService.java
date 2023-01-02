package com.wangkisa.commerce.domain.order.service;

import com.wangkisa.commerce.domain.order.dto.OrderDTO;

public interface OrderService {

    OrderDTO.ResOrderInfo registerOrder(OrderDTO.ReqRegisterOrder reqRegisterOrder, Long userId);

    OrderDTO.ResOrderInfo purchaseOrder(OrderDTO.ReqPurchaseOrder reqPurchaseOrder, Long userId);
}
