package com.kupchyk.javaee.service;

import com.kupchyk.javaee.model.Book;
import com.kupchyk.javaee.model.User;
import com.kupchyk.javaee.repository.BookRepository;
import com.kupchyk.javaee.validation.BookValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookValidator bookValidator;


    @Transactional
    public String createNewBook(final Book newBook) {
        log.info("Try to create new book: {}", newBook.getIsbn());
        String validatorResult = bookValidator.validateNewBook(newBook);
        if(validatorResult.equals("OK")) {
            final Book book = bookRepository.save(newBook);
            log.info("New book is created: {}", book);
        }
        return validatorResult;

    }

    @Transactional
    public List<Book> returnAllBooks(){
        return  bookRepository.findAll();
    }

    @Transactional
    public List<Book> findBooks(final String s){
        return bookRepository.findAllWhereTitleLikeOrAuthorLikeOrIsbnLike(s);
    }

    @Transactional
    public Book getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

}

