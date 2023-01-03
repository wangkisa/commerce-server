package com.wangkisa.commerce.domain.order.repository;

import com.wangkisa.commerce.domain.order.entity.Order;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderCustomRepository {

    Optional<Order> findByUUID(UUID id);
}
