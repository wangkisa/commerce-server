package com.wangkisa.commerce.domain.order.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wangkisa.commerce.domain.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static com.wangkisa.commerce.domain.order.entity.QOrder.order;

@RequiredArgsConstructor
@Repository
public class OrderCustomRepositoryImpl implements OrderCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Optional<Order> findByUUID(UUID id) {
        Order orderEntity = jpaQueryFactory.selectFrom(order)
                .where(order.id.eq(id))
                .fetchOne();
        return Optional.of(orderEntity);
    }
}
