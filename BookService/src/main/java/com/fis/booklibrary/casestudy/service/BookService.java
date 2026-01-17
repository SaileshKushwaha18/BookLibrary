package com.fis.booklibrary.casestudy.service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fis.booklibrary.casestudy.model.Book;
import com.fis.booklibrary.casestudy.repository.BookRepository;

@Service
public class BookService {
	
	private static final Logger logger = Logger.getLogger(BookService.class.getName());

	@Autowired
	private BookRepository bookRepository;
	
	public List<Book> getBooks(){
		return bookRepository.findAll();
	}
	
	public Optional<Book> getBook(String bookId){
		return bookRepository.findById(bookId);
	}

	public Book updateCopiesAvailable(String bookId, Integer remainingCopies) {
		Optional<Book> book = bookRepository.findById(bookId);
		
		if(book.isPresent()) {
			book.get().setCopiesAvailable(remainingCopies);
			logger.info("Updated book copies for bookId: " + bookId + " to: " + remainingCopies);
			return bookRepository.save(book.get());
		} else {
			logger.warning("Book not found with bookId: " + bookId);
			throw new IllegalArgumentException("Book not found with bookId: " + bookId);
		}
	}
}
