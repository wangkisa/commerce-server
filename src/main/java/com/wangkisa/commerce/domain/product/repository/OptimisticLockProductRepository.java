package com.wangkisa.commerce.domain.product.repository;

import com.wangkisa.commerce.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface OptimisticLockProductRepository extends JpaRepository<Product, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    Optional<Product> findById(Long id);
}
