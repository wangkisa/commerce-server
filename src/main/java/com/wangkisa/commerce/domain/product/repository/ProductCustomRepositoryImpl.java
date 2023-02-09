package com.wangkisa.commerce.domain.product.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wangkisa.commerce.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static com.wangkisa.commerce.domain.product.entity.QProduct.product;

@RequiredArgsConstructor
@Repository
public class ProductCustomRepositoryImpl implements ProductCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Product> findAllByPageRequestDTO(Pageable pageable) {
        QueryResults<Product> productQueryResults = jpaQueryFactory.selectFrom(product)
                .orderBy(new OrderSpecifier<>(Order.DESC, product.createdAt))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(productQueryResults.getResults(), pageable, productQueryResults.getTotal());
    }

//    @Override
//    public Product getByProductId(long productId) {
//        return jpaQueryFactory.selectFrom(product)
//                .where(product.id.eq(productId))
//                .fetchOne();
//    }
}
