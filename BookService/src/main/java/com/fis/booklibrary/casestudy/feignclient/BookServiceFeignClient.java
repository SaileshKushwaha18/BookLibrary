package com.fis.booklibrary.casestudy.feignclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fis.booklibrary.casestudy.model.Book;

/**
 * Feign client for Book Service communication.
 * Uses modern Spring Boot 3 annotations (@GetMapping, @PutMapping)
 * Follows REST standards with proper HTTP methods and media types.
 */
@FeignClient(name = "book-service", url = "http://book-service")
@RequestMapping(value = "/books", produces = MediaType.APPLICATION_JSON_VALUE)
public interface BookServiceFeignClient {
    
    /**
     * GET: Retrieve all books
     * REST Standard: Safe, idempotent, returns 200 OK
     */
    @GetMapping
    ResponseEntity<List<Book>> getBooks();
 
    /**
     * GET: Retrieve a specific book by ID
     * REST Standard: Safe, idempotent, returns 200 OK or 404 NOT FOUND
     * @param bookId unique book identifier
     * @return Book details or 404 if not found
     */
    @GetMapping("/{bookId}")
    ResponseEntity<Book> getBook(@PathVariable("bookId") String bookId);
    
    /**
     * PUT: Update book's available copies
     * REST Standard: Idempotent, returns 200 OK or 404 NOT FOUND
     * @param bookId unique book identifier
     * @param remainingCopies number of copies available
     * @return Updated book details or 404 if not found
     */
    @PutMapping("/{bookId}")
    ResponseEntity<Book> updateBook(@PathVariable("bookId") String bookId, @RequestBody Integer remainingCopies);
}