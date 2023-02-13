package com.wangkisa.commerce.domain.product.facade;

import com.wangkisa.commerce.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
@Slf4j
public class RedissonLockProductFacade {

    private final RedissonClient redissonClient;
    private final ProductService productService;
    public void synchronizedSubtractQuantity(final Long productId, final Integer quantity) throws InterruptedException {
        final RLock lock = redissonClient.getLock(productId.toString());
        try {
            boolean isAvailable = lock.tryLock(10, 1, TimeUnit.SECONDS);
            if (!isAvailable) {
                log.error("redisson getLock timeout");
                return;
            }
            productService.synchronizedSubtractQuantity2(productId, quantity);
        } finally {
            // unlock the lock object
            lock.unlock();
        }
    }
}
