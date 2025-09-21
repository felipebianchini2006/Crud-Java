package org.example.library.web;

import org.example.library.book.Book;
import org.example.library.book.BookService;
import org.example.library.security.CustomUserDetailsService;
import org.example.library.user.User;
import org.example.library.user.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookWebController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public String listBooks(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "12") int size,
                           @RequestParam(required = false) String search,
                           @RequestParam(required = false) String genre,
                           @RequestParam(required = false) String author,
                           Authentication authentication,
                           Model model) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books;
        
        if (search != null && !search.trim().isEmpty()) {
            books = bookService.searchBooks(search.trim(), pageable);
            model.addAttribute("search", search);
        } else if (genre != null && !genre.trim().isEmpty()) {
            books = bookService.findByGenre(genre.trim(), pageable);
            model.addAttribute("genre", genre);
        } else if (author != null && !author.trim().isEmpty()) {
            books = bookService.findByAuthor(author.trim(), pageable);
            model.addAttribute("author", author);
        } else {
            books = bookService.findAllPaginated(pageable);
        }
        
        model.addAttribute("books", books);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", books.getTotalPages());
        model.addAttribute("totalElements", books.getTotalElements());
        
        // Get unique genres and authors for filters
        model.addAttribute("genres", bookService.findAllGenres());
        model.addAttribute("authors", bookService.findAllAuthors());
        
        // Check if user is logged in and can borrow books
        boolean canBorrow = false;
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getName().equals("anonymousUser")) {
            canBorrow = true;
        }
        model.addAttribute("canBorrow", canBorrow);
        
        return "books/list";
    }

    @GetMapping("/{id}")
    public String bookDetails(@PathVariable Long id, 
                             Authentication authentication, 
                             Model model) {
        Book book = bookService.findById(id);
        model.addAttribute("book", book);
        
        // Check if user can borrow this book
        boolean canBorrow = false;
        boolean isOwner = false;
        
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getName().equals("anonymousUser")) {
            
            CustomUserDetailsService.CustomUserPrincipal principal = 
                (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
            User currentUser = principal.getUser();
            
            canBorrow = book.isAvailable() && 
                       (currentUser.getRole() == UserRole.READER || 
                        currentUser.getRole() == UserRole.ADMIN);
            
            // Check if current user is the author of this book
            if (book.getAuthorUser() != null) {
                isOwner = book.getAuthorUser().getId().equals(currentUser.getId()) ||
                         currentUser.getRole() == UserRole.ADMIN;
            }
        }
        
        model.addAttribute("canBorrow", canBorrow);
        model.addAttribute("isOwner", isOwner);
        
        return "books/details";
    }
}
