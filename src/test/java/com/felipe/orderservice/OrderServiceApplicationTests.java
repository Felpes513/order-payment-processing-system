package com.felipe.orderservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.assertj.core.api.Assertions.assertThat;

class OrderServiceApplicationTests {

	@Test
	void applicationClassHasSpringBootConfiguration() {
		assertThat(OrderServiceApplication.class)
				.hasAnnotation(SpringBootApplication.class);
	}

}
