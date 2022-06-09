package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;

import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.RequestMapping;

import reactor.core.publisher.Mono;

@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
			
				.route(p -> p.path("/Students/**").filters(
						f -> f.circuitBreaker(config -> config
								.setName("mycmd")
								.setFallbackUri("forward:/fallback")))
						.uri("lb://students"))
				.route(p -> p.path("/students/**")
						.uri("lb://uni_registration"))
				.route(p -> p.path("/financial-statement/**")
						.uri("lb://financial-statement"))
				.build();
	}

	@RequestMapping("/fallback")
	public Mono<String> fallback() {
		return Mono.just(" problem has occured fallback");
	}
}
