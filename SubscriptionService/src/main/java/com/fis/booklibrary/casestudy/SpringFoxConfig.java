package com.fis.booklibrary.casestudy;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI (Swagger 3) configuration for SubscriptionService
 */
@Configuration
public class SpringFoxConfig {

	@Bean
	public OpenAPI subscriptionServiceOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("SubscriptionService REST API")
						.description("This service provides information about the book subscription")
						.version("1.0.0")
						.contact(new Contact()
								.name("Sailesh Kushwaha")
								.url("https://www.booklibrary.com")
								.email("sailesh.kushwaha@fisglobal.com"))
						.license(new License()
								.name("Apache 2.0")
								.url("https://www.apache.org/licenses/LICENSE-2.0.html")));
	}
}