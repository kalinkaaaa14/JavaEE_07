package com.kupchyk.javaee.validation;

import com.kupchyk.javaee.model.Book;
import com.kupchyk.javaee.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookValidator {
    private final BookRepository bookRepository;

    public String validateNewBook(final Book newBook) {
        List<Book> books = bookRepository.findAll();

        if(newBook.getIsbn().isBlank() || newBook.getAuthor().isBlank() || newBook.getTitle().isBlank()){
            return "All fields should be filled!";
        }
        for (Book book : books) {
            if(book.getIsbn().equals(newBook.getIsbn())){
                return "The book with such ISBN already exists. It should be unique. Try again.";
            }
        }
        return "OK";
    }

}
