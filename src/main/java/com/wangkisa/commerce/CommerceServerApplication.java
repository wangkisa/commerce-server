package com.wangkisa.commerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CommerceServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommerceServerApplication.class, args);
	}

}
