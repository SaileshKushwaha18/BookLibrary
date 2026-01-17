package com.fis.booklibrary.casestudy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="BOOK")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Book {
	
	@Id
	@Column(name="BOOK_ID")
	private String bookId;
	
	@Column(name="BOOK_NAME")
	private String bookName;
	
	@Column(name="AUTHOR")
	private String author;
	
	@Column(name="AVAILABLE_COPIES")
	private int copiesAvailable;
	
	@Column(name="TOTAL_COPIES")
	private int totalCopies;
}
