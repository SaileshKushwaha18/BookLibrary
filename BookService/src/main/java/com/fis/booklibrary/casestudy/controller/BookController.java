package com.fis.booklibrary.casestudy.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fis.booklibrary.casestudy.feignclient.BookServiceFeignClient;
import com.fis.booklibrary.casestudy.model.Book;
import com.fis.booklibrary.casestudy.service.BookService;

/**
 * REST Controller for Book management API.
 * Follows REST standards with proper HTTP methods, status codes, and error handling.
 * Uses modern Spring Boot 3 annotations.
 */
@RestController
@RequestMapping(value = "/books", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookController implements BookServiceFeignClient {

	@Autowired
	private BookService bookService;
	
	/**
	 * GET: Retrieve all books
	 * HTTP Method: GET (safe, idempotent)
	 * Status Code: 200 OK
	 * @return List of all books
	 */
	@Override
	@GetMapping
	public ResponseEntity<List<Book>> getBooks() {
		List<Book> books = bookService.getBooks();
		return ResponseEntity.ok(books);
	}
	
	/**
	 * GET: Retrieve a specific book by ID
	 * HTTP Method: GET (safe, idempotent)
	 * Status Codes: 200 OK, 404 NOT FOUND
	 * @param bookId unique book identifier
	 * @return Book details or 404 error
	 */
	@Override
	@GetMapping("/{bookId}")
	public ResponseEntity<Book> getBook(@PathVariable("bookId") String bookId) {
		try {
			Book book = bookService.getBook(bookId)
				.orElseThrow(() -> new ResponseStatusException(
					HttpStatus.NOT_FOUND, 
					"Book not found with ID: " + bookId
				));
			return ResponseEntity.ok(book);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND, 
				"Book not found with ID: " + bookId, 
				e
			);
		}
	}
	
	/**
	 * PUT: Update book's available copies
	 * HTTP Method: PUT (idempotent, safe for multiple calls)
	 * Status Codes: 200 OK, 400 BAD REQUEST, 404 NOT FOUND
	 * @param bookId unique book identifier
	 * @param remainingCopies number of copies available (must be >= 0)
	 * @return Updated book details or error
	 */
	@Override
	@PutMapping("/{bookId}")
	public ResponseEntity<Book> updateBook(
			@PathVariable("bookId") String bookId,
			@RequestBody Integer remainingCopies) {
		
		// Validation: Check if remainingCopies is valid
		if (remainingCopies == null || remainingCopies < 0) {
			throw new ResponseStatusException(
				HttpStatus.BAD_REQUEST,
				"Invalid request: remainingCopies must be a non-negative integer"
			);
		}
		
		try {
			Book updatedBook = bookService.updateCopiesAvailable(bookId, remainingCopies);
			return ResponseEntity.ok(updatedBook);
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND,
				"Book not found with ID: " + bookId,
				e
			);
		}
	}
}
