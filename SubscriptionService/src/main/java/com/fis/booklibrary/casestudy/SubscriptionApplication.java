package com.fis.booklibrary.casestudy;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;

import com.fis.booklibrary.casestudy.model.Subscription;
import com.fis.booklibrary.casestudy.repository.SubscriptionRepository;

@SpringBootApplication
@EnableDiscoveryClient
public class SubscriptionApplication implements CommandLineRunner {

	@Autowired
	private SubscriptionRepository subscriptionRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(SubscriptionApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<Subscription> subscriptions = new ArrayList<>();
		Subscription s1 = new Subscription(null, "John", "12-JUN-2020", null, "B1212");
		Subscription s2 = new Subscription(null, "Mark", "26-APR-2020", "14-May-2020", "B4232");
		Subscription s3 = new Subscription(null, "Peter", "22-JUN-2020", null, "B1212");
		subscriptions.add(s1);
		subscriptions.add(s2);
		subscriptions.add(s3);
		
		subscriptionRepository.saveAll(subscriptions);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder
				.setConnectTimeout(java.time.Duration.ofSeconds(5))
				.setReadTimeout(java.time.Duration.ofSeconds(10))
				.build();
	}
}
