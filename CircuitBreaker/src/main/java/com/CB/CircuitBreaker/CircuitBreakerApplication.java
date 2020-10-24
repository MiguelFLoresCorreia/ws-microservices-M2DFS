package com.CB.CircuitBreaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableCircuitBreaker
@EnableEurekaClient
public class CircuitBreakerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CircuitBreakerApplication.class, args);
	}

}
