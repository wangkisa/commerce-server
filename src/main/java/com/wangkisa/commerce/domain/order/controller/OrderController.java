package com.wangkisa.commerce.domain.order.controller;

import com.wangkisa.commerce.domain.common.response.ApiResponse;
import com.wangkisa.commerce.domain.order.dto.OrderDTO;
import com.wangkisa.commerce.domain.order.service.OrderService;
import com.wangkisa.commerce.domain.user.entity.User;
import com.wangkisa.commerce.security.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/order")
@RestController
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "주문서 등록")
    @PostMapping(value = "/registerOrder", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<OrderDTO.ResOrderInfo> registerOrder(@Validated @RequestBody OrderDTO.ReqRegisterOrder reqRegisterOrder,
                                                            @Parameter(hidden = true) @AuthUser User user) {
        return ApiResponse.success(orderService.registerOrder(reqRegisterOrder, user.getId()));
    }
}
