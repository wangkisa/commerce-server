package com.wangkisa.commerce.domain.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OptimisticLockProductFacade {

    private final ProductService productService;

    public void optimisticLockSubtractQuantity(long id, Integer quantity) throws InterruptedException {
        while (true) {
            try {
                productService.optimisticLockSubtractQuantity(id, quantity);
                break;
            } catch (Exception e) {
                // retry
                log.error("OPTIMISTIC LOCK VERSION CONFLICT !!!");
                log.error(e.getMessage());
                Thread.sleep(1);
            }
        }
    }
}
