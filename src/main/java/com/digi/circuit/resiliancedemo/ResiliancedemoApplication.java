package com.digi.circuit.resiliancedemo;

import io.prometheus.client.CollectorRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ResiliancedemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResiliancedemoApplication.class, args);
	}

	@Bean
	public CollectorRegistry collectorRegistry() {
		return CollectorRegistry.defaultRegistry;
	}

}

