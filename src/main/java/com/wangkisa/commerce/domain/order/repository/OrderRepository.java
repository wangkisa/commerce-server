package com.wangkisa.commerce.domain.order.repository;

import com.wangkisa.commerce.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, OrderCustomRepository {

//    Optional<Order> findById(UUID uuid);
}
