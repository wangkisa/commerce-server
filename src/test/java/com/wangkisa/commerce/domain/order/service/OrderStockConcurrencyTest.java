package com.wangkisa.commerce.domain.order.service;

import com.wangkisa.commerce.configuration.TestConfig;
import com.wangkisa.commerce.domain.product.entity.Product;
import com.wangkisa.commerce.domain.product.repository.ProductRepository;
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

import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Import(TestConfig.class)
public class OrderStockConcurrencyTest {

    private final int threadCount = 10;
    private long productId;
    private final Integer quantity = 1;
    private final Integer initQuantity = 10;
    private ExecutorService executorService;
    private CountDownLatch countDownLatch;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

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
    @DisplayName("SYNCHRONIZED를 사용한 재고 감소 테스트")
    void synchronizedSubtractQuantityTest() throws InterruptedException {
        // given
        System.out.println("test productId id = " + productId);
        sleep(1000);

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

}
