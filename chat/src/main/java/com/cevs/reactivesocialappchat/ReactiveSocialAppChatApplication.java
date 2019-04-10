package com.cevs.reactivesocialappchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.ComponentScan;

@SpringCloudApplication
@EnableCircuitBreaker
@ComponentScan({"com.cevs.reactivesocialappchat.repositories", "com.cevs.reactivesocialappchat"})
public class ReactiveSocialAppChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveSocialAppChatApplication.class, args);
	}

}
