package com.wangkisa.commerce.domain.product.repository;

import com.wangkisa.commerce.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCustomRepository {

    Page<Product> findAllByPageRequestDTO(Pageable pageable);
}
