package com.customer.loyalty.program.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenAPIConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().info(new Info().title("Customer Rewards API").version("1.0")
				.description("API documentation for customer reward point system").contact(apiContact())
				.license(apiLicense()));
	}

	private io.swagger.v3.oas.models.info.License apiLicense() {
		return new io.swagger.v3.oas.models.info.License().name("CHC Account Licensed").url("https://google.com");
	}

	private io.swagger.v3.oas.models.info.Contact apiContact() {
		return new io.swagger.v3.oas.models.info.Contact().name("CHC Account Licensed").email("chc@gmail.com");
	}
}