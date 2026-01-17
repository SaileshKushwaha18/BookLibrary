package com.fis.booklibrary.casestudy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Subscription {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="SUBSCRIPTION_ID")
	private Long id;
	
	@Column(name="SUBSCRIBER_NAME")
	private String subscriberName;
	
	@Column(name="DATE_SUBSCRIBED")
	private String dateSubscriber;
	
	@Column(name="DATE_RETURNED")
	private String dateReturned;

	@Column(name="BOOK_ID")
	private String bookId;
}
