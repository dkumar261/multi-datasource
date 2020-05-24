package com.multidatasource.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multidatasource.repository.BookRepository;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;
}
