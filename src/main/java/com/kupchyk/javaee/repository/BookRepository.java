package com.kupchyk.javaee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.kupchyk.javaee.model.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    Book findByIsbn(String isbn);

    @Query("SELECT b FROM Book b WHERE lower(b.title) LIKE %:search% OR lower(b.author) LIKE %:search% OR lower(b.isbn) LIKE %:search%")
    List<Book> findAllWhereTitleLikeOrAuthorLikeOrIsbnLike(@Param("search") String search);
}
