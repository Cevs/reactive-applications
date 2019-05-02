package com.cevs.reactive.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@SpringCloudApplication
public class ReactiveChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveChatApplication.class, args);
	}

}
