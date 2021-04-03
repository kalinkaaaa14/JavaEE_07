package com.kupchyk.javaee.controller;

import com.kupchyk.javaee.model.Book;
import com.kupchyk.javaee.model.User;
import com.kupchyk.javaee.service.BookService;
import com.kupchyk.javaee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
public class BookController {

    BookService bookService;
    UserService userService;

    @Autowired
    public BookController(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String getMain(Model model) {
        model.addAttribute("books", bookService.returnAllBooks());
        return "main";
    }


    @GetMapping("/add-book")
    public String addBookGt() {
       return "add-book";
    }

    @ResponseBody
    @PostMapping("/add-book")
    public ResponseEntity<String> addBookPost(@RequestBody Book book) {
        String isValid = bookService.createNewBook(book);
        int status = isValid.equals("OK") ? 200 : 400;
        return ResponseEntity.status(status).body(isValid);
    }

    @ResponseBody
    @GetMapping("/get-books")
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.status(200).body(bookService.returnAllBooks());
    }

    @ResponseBody
    @GetMapping("/search-books")
    public ResponseEntity<List<Book>> searchBook(@RequestParam(name = "getBy", required = false) String search) {
        if(search == null) {
            return ResponseEntity.status(200)
                    .header("h1","Getting all books")
                    .body(bookService.returnAllBooks());
        }
        return ResponseEntity.ok().body(bookService.findBooks(search));
    }

    @GetMapping("/book/{isbn}")
    public String getBook(@PathVariable String isbn, Model model) {
        Book book = bookService.getBookByIsbn(isbn);
        model.addAttribute("book", book);
        return "view-book";
    }

@GetMapping("/add-fav/{isbn}")
public String addFav(@PathVariable String isbn, @AuthenticationPrincipal UserDetails currentUser){
    System.out.println("ISBN "+isbn);
    Optional<User> user = userService.findUserByUsername(currentUser.getUsername());
    Book book = bookService.getBookByIsbn(isbn);
    userService.fav(user,book);
    return "redirect:/favorite-books";
}

    @GetMapping("/favorite-books")
    public String getFavouriteBooks(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        System.out.println(currentUser);
        Optional<User> user = userService.findUserByUsername(currentUser.getUsername());
        model.addAttribute("books", user.get().getFavoriteBooks());
        return "favoriteBooks";
    }

    @PostMapping("/del-fav")
    public String deleteFavouriteBook(@RequestParam(name = "isbn") String isbn, @AuthenticationPrincipal UserDetails currentUser) {
        Optional<User> user = userService.findUserByUsername(currentUser.getUsername());
        Book book = bookService.getBookByIsbn(isbn);
        userService.favDel(user,book);
        return "redirect:/favorite-books";
    }

}
