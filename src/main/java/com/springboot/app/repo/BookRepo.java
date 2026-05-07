package com.springboot.app.repo;

import com.springboot.app.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookRepo extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
}
