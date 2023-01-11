package com.multidatasource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.multidatasource.domain.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

}
