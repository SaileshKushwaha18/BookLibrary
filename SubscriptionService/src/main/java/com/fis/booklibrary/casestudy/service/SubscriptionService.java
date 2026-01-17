package com.fis.booklibrary.casestudy.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import com.fis.booklibrary.casestudy.model.Book;
import com.fis.booklibrary.casestudy.model.Subscription;
import com.fis.booklibrary.casestudy.repository.SubscriptionRepository;

@Service
public class SubscriptionService {
	
	private static final Logger logger = Logger.getLogger(SubscriptionService.class.getName());
	private static final String BOOK_SERVICE_URI = "http://book-service/books/";

	@Autowired
	private SubscriptionRepository subscriptionRepository;

	@Autowired
	private RestTemplate restTemplate;

	public List<Subscription> getSubscriptions() {
		return subscriptionRepository.findAll();
	}

	@Transactional
	public Subscription addSubscription(Subscription subscription) {
		return subscriptionRepository.save(subscription);
	}

	@CircuitBreaker(name = "book-service", fallbackMethod = "getAvailableCopiesFallback")
	public int getAvailableCopies(Subscription subscription) {
		try {
			Book book = restTemplate.getForObject(BOOK_SERVICE_URI + subscription.getBookId(), Book.class);
			return book != null ? book.getCopiesAvailable() : 0;
		} catch (RestClientException e) {
			logger.log(Level.WARNING, "Error calling book-service for bookId: " + subscription.getBookId(), e);
			throw new RuntimeException("Failed to fetch book availability", e);
		}
	}

	public int getAvailableCopiesFallback(Subscription subscription, Exception ex) {
		logger.log(Level.SEVERE, "Circuit breaker activated for book-service: " + ex.getMessage());
		return 0;
	}

	@Transactional
	@CircuitBreaker(name = "book-service", fallbackMethod = "updateAvailableCopiesFallback")
	public ResponseEntity<String> updateAvailableCopies(Subscription subscription) {
		int remainingCopies = getAvailableCopies(subscription);
		int updatedRemainingCopies;

		if (remainingCopies <= 0 && subscription.getDateReturned() == null) {
			logger.warning("Book copies not available for subscription. BookId: " + subscription.getBookId());
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
					.body("Book copies not available for subscription");
		}

		if (remainingCopies >= 0) {
			if (subscription.getDateReturned() == null) {
				updatedRemainingCopies = remainingCopies - 1;
			} else {
				updatedRemainingCopies = remainingCopies + 1;
			}

			try {
				restTemplate.put(BOOK_SERVICE_URI + subscription.getBookId(), updatedRemainingCopies);
				logger.info("Successfully updated book copies for bookId: " + subscription.getBookId());
				return ResponseEntity.ok(subscription.toString());
			} catch (RestClientException e) {
				logger.log(Level.SEVERE, "Error updating book availability: " + e.getMessage());
				throw new RuntimeException("Failed to update book availability", e);
			}
		}

		return ResponseEntity.ok(null);
	}

	public ResponseEntity<String> updateAvailableCopiesFallback(Subscription subscription, Exception ex) {
		logger.log(Level.SEVERE, "Circuit breaker activated during update: " + ex.getMessage());
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body("Book service is temporarily unavailable. Please try again later - " + LocalDateTime.now()
						.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
	}
}
