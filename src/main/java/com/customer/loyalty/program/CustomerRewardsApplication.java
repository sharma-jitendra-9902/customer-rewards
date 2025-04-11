package com.customer.loyalty.program;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.customer.loyalty.program")
public class CustomerRewardsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerRewardsApplication.class, args);
	}

}
