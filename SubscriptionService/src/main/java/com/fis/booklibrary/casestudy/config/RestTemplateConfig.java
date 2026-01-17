package com.fis.booklibrary.casestudy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;

/**
 * RestTemplate configuration with timeout and connection settings
 */
@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder
				.setConnectTimeout(java.time.Duration.ofSeconds(5))
				.setReadTimeout(java.time.Duration.ofSeconds(10))
				.build();
	}
}
