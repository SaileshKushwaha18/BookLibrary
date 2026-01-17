package com.fis.booklibrary.casestudy.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Cloud Gateway configuration replacing deprecated Zuul proxy
 */
@Configuration
public class GatewayConfig {

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				// Route for BookService
				.route("book-service", r -> r
						.path("/book-service/**")
						.filters(f -> f.stripPrefix(1)
								.addRequestHeader("X-Gateway-Route", "book-service"))
						.uri("lb://book-service"))
				
				// Route for SubscriptionService
				.route("subscription-service", r -> r
						.path("/subscription-service/**")
						.filters(f -> f.stripPrefix(1)
								.addRequestHeader("X-Gateway-Route", "subscription-service"))
						.uri("lb://subscription-service"))
				.build();
	}
}
