package com.fis.booklibrary.casestudy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fis.booklibrary.casestudy.model.Subscription;
import com.fis.booklibrary.casestudy.service.SubscriptionService;

/**
 * REST Controller for Subscription management API.
 * Follows REST standards with proper HTTP methods, status codes, and error handling.
 * Implements circuit breaker pattern with graceful fallback.
 * Uses modern Spring Boot 3 annotations.
 */
@RestController
@RequestMapping(value = "/subscriptions", produces = MediaType.APPLICATION_JSON_VALUE)
public class SubscriptionController {

	@Autowired
	private SubscriptionService subscriptionService;

	/**
	 * GET: Retrieve all subscriptions
	 * HTTP Method: GET (safe, idempotent)
	 * Status Code: 200 OK
	 * Notes: Internally calls Book Service via circuit breaker
	 * @return List of all subscriptions
	 */
	@GetMapping
	public ResponseEntity<List<Subscription>> getSubscriptions() {
		List<Subscription> subscriptions = subscriptionService.getSubscriptions();
		return ResponseEntity.ok(subscriptions);
	}
	
	/**
	 * GET: Retrieve a specific subscription by ID
	 * HTTP Method: GET (safe, idempotent)
	 * Status Codes: 200 OK, 404 NOT FOUND
	 * @param id unique subscription identifier
	 * @return Subscription details or 404 error
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Subscription> getSubscription(@PathVariable("id") Long id) {
		Subscription subscription = subscriptionService.getSubscriptionById(id)
			.orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND,
				"Subscription not found with ID: " + id
			));
		return ResponseEntity.ok(subscription);
	}
	
	/**
	 * POST: Create a new subscription
	 * HTTP Method: POST (creates new resource, non-idempotent)
	 * Status Codes: 201 CREATED, 400 BAD REQUEST, 422 UNPROCESSABLE ENTITY
	 * Notes: Circuit breaker protects Book Service call with graceful fallback
	 * @param subscription subscription details to create
	 * @return Created subscription with 201 status code or error
	 * @throws ResponseStatusException with 400 if validation fails
	 * @throws ResponseStatusException with 422 if book copies unavailable
	 */
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Subscription> createSubscription(@RequestBody Subscription subscription) {
		
		// Validation: Check required fields
		if (subscription == null) {
			throw new ResponseStatusException(
				HttpStatus.BAD_REQUEST,
				"Invalid request: subscription data is required"
			);
		}
		
		if (subscription.getSubscriberName() == null || subscription.getSubscriberName().trim().isEmpty()) {
			throw new ResponseStatusException(
				HttpStatus.BAD_REQUEST,
				"Invalid request: subscriberName is required and cannot be empty"
			);
		}
		
		if (subscription.getDateSubscriber() == null) {
			throw new ResponseStatusException(
				HttpStatus.BAD_REQUEST,
				"Invalid request: dateSubscriber is required"
			);
		}
		
		if (subscription.getBookId() == null || subscription.getBookId().trim().isEmpty()) {
			throw new ResponseStatusException(
				HttpStatus.BAD_REQUEST,
				"Invalid request: bookId is required and cannot be empty"
			);
		}
		
		// Check book availability (circuit breaker protected)
		try {
			ResponseEntity<String> availabilityCheck = subscriptionService.updateAvailableCopies(subscription);
			if (availabilityCheck.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
				throw new ResponseStatusException(
					HttpStatus.UNPROCESSABLE_ENTITY,
					"Book copies are not available for subscription"
				);
			}
		} catch (ResponseStatusException e) {
			throw e;
		}
		
		// Create subscription and return 201 CREATED
		Subscription createdSubscription = subscriptionService.addSubscription(subscription);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdSubscription);
	}
}
