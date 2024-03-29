package com.wangkisa.commerce.domain.order.service;

import com.wangkisa.commerce.configuration.TestConfig;
import com.wangkisa.commerce.domain.product.entity.Product;
import com.wangkisa.commerce.domain.product.facade.RedissonLockProductFacade;
import com.wangkisa.commerce.domain.product.repository.ProductRepository;
import com.wangkisa.commerce.domain.product.facade.OptimisticLockProductFacade;
import com.wangkisa.commerce.domain.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Import(TestConfig.class)
public class OrderStockConcurrencyTest {

    private final int threadCount = 300;
    private long productId;
    private final Integer quantity = 1;
    private final Integer initQuantity = 300;
    private ExecutorService executorService;
    private CountDownLatch countDownLatch;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    @Autowired
    private OptimisticLockProductFacade optimisticLockProductFacade;

    @Autowired
    private RedissonLockProductFacade redissonLockProductFacade;

    @BeforeEach
    void setUp() {
        executorService = Executors.newFixedThreadPool(threadCount);
        countDownLatch = new CountDownLatch(threadCount);
        Product product = productRepository.save(Product.builder()
                .name("테스트 상품")
                .color("red")
                .quantity(initQuantity)
                .price(BigDecimal.valueOf(3000))
                .build());
        productId = product.getId();
    }

    @Test
    @DisplayName("멀티쓰레드를 사용한 재고 감소 실패 테스트")
    void multiThreadSubtractQuantityTest() throws InterruptedException {
        IntStream.range(0, threadCount).forEach(e -> executorService.submit(() -> {
                    try {
                        productService.subtractQuantity(productId, quantity);
                    }
                    catch (Exception error) {
                        System.out.println("error = " + error);
                    }
                    finally {
                        countDownLatch.countDown();
                    }
                }
        ));

        countDownLatch.await();

        // then
        final Integer afterQuantity = productRepository.findById(productId).get().getQuantity();
        System.out.println("멀티쓰레드 처리 이후 수량: " + afterQuantity);
        // 멀티쓰레드로 재고 감소를 하다보면 270, 280 등 수량이 계속해서 맞지 않고 재고 수량이 0이 되지 않는다.
        assertThat(afterQuantity).isNotZero();
        assertThat(afterQuantity).isNotEqualTo(initQuantity);
    }

    @Test
    @DisplayName("SYNCHRONIZED를 사용한 재고 감소 테스트")
    void synchronizedSubtractQuantityTest() throws InterruptedException {
        // given
        // when
        IntStream.range(0, threadCount).forEach(e -> executorService.submit(() -> {
                    try {
                        productService.synchronizedSubtractQuantity(productId, quantity);
                    }
                    catch (Exception error) {
                        System.out.println("error = " + error);
                    }
                    finally {
                        countDownLatch.countDown();
                    }
                }
        ));

        countDownLatch.await();

        // then
        final Integer afterQuantity = productRepository.findById(productId).get().getQuantity();
        System.out.println("SYNCHRONIZED 동시성 처리 이후 수량: " + afterQuantity);
        assertThat(afterQuantity).isZero();
    }

    @DisplayName("pessimistic lock을 사용한 재고 감소 테스트")
    @Test
    void pessimisticLockSubtractQuantityTest() throws InterruptedException {
        // given
        // when
        IntStream.range(0, threadCount).forEach(e -> executorService.submit(() -> {
                try {
                    productService.pessimisticLockSubtractQuantity(productId, quantity);
                } finally {
                    countDownLatch.countDown();
                }
            }
        ));
        countDownLatch.await();
        // then
        final Integer afterQuantity = productRepository.findById(productId).get().getQuantity();
        System.out.println("PESSIMISTIC LOCK 동시성 처리 이후 수량:" + afterQuantity);
        assertThat(afterQuantity).isZero();
    }

    @DisplayName("optimistic lock을 사용한 재고 감소 테스트")
    @Test
    void optimisticLockSubtractQuantityTest() throws InterruptedException {
        // given
        // when
        IntStream.range(0, threadCount).forEach(e -> executorService.submit(() -> {
                try {
                    optimisticLockProductFacade.optimisticLockSubtractQuantity(productId, quantity);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                } finally {
                    countDownLatch.countDown();
                }
            }
        ));
        countDownLatch.await();
        // then
        final Integer afterQuantity = productRepository.findById(productId).get().getQuantity();
        System.out.println("OPTIMISTIC LOCK 동시성 처리 이후 수량:" + afterQuantity);
        assertThat(afterQuantity).isZero();
    }

    @DisplayName("redis reddison lock 을 사용한 재고 감소 테스트")
    @Test
    void reddisonLockSubtractQuantityTest() throws InterruptedException {
        // given
        // when
        IntStream.range(0, threadCount).forEach(e -> executorService.submit(() -> {
                    try {
                        redissonLockProductFacade.synchronizedSubtractQuantity(productId, quantity);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    } finally {
                        countDownLatch.countDown();
                    }
                }
        ));
        countDownLatch.await();
        // then
        final Integer afterQuantity = productRepository.findById(productId).get().getQuantity();
        System.out.println("REDDISON LOCK 동시성 처리 이후 수량:" + afterQuantity);
        assertThat(afterQuantity).isZero();
    }

}
