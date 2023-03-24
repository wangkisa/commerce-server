package com.wangkisa.commerce;

import com.wangkisa.commerce.configuration.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestConfig.class)
class CommerceServerApplicationTests {

	@Test
	void contextLoads() {
	}

}
