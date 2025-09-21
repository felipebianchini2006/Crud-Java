package org.example.library.web;

import org.example.library.book.Book;
import org.example.library.book.BookService;
import org.example.library.loan.LoanService;
import org.example.library.security.CustomUserDetailsService;
import org.example.library.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/author")
public class AuthorController {

    @Autowired
    private BookService bookService;

    @Autowired
    private LoanService loanService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        CustomUserDetailsService.CustomUserPrincipal principal = 
            (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();
        
        model.addAttribute("user", user);
        
        // Get author's books
        List<Book> authorBooks = bookService.findByAuthorUser(user);
        model.addAttribute("authorBooks", authorBooks);
        
        // Calculate statistics
        long totalBooks = authorBooks.size();
        long availableBooks = authorBooks.stream().mapToLong(book -> book.isAvailable() ? 1 : 0).sum();
        long borrowedBooks = totalBooks - availableBooks;
        
        model.addAttribute("totalBooks", totalBooks);
        model.addAttribute("availableBooks", availableBooks);
        model.addAttribute("borrowedBooks", borrowedBooks);
        
        return "author/dashboard";
    }

    @GetMapping("/books")
    public String manageBooks(Authentication authentication, Model model) {
        CustomUserDetailsService.CustomUserPrincipal principal = 
            (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();
        
        model.addAttribute("user", user);
        model.addAttribute("books", bookService.findByAuthorUser(user));
        
        return "author/books";
    }

    @GetMapping("/books/new")
    public String newBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "author/book-form";
    }

    @PostMapping("/books")
    public String createBook(@RequestParam String title,
                           @RequestParam String author,
                           @RequestParam(required = false) String genre,
                           @RequestParam(required = false) String description,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {
        
        CustomUserDetailsService.CustomUserPrincipal principal = 
            (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();
        
        try {
            Book book = bookService.createBook(title, author, genre, description, user);
            redirectAttributes.addFlashAttribute("success", "Livro criado com sucesso!");
            return "redirect:/author/books/" + book.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao criar livro: " + e.getMessage());
            return "redirect:/author/books/new";
        }
    }

    @GetMapping("/books/{id}")
    public String bookDetails(@PathVariable Long id, 
                             Authentication authentication, 
                             Model model) {
        CustomUserDetailsService.CustomUserPrincipal principal = 
            (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();
        
        Book book = bookService.findById(id);
        
        // Check if user owns this book
        if (book.getAuthorUser() == null || !book.getAuthorUser().getId().equals(user.getId())) {
            return "redirect:/author/books";
        }
        
        model.addAttribute("book", book);
        
        // Get loans for this book
        try {
            model.addAttribute("bookLoans", loanService.findByBook(id));
        } catch (Exception e) {
            model.addAttribute("bookLoans", java.util.Collections.emptyList());
        }
        
        return "author/book-details";
    }

    @GetMapping("/books/{id}/edit")
    public String editBookForm(@PathVariable Long id, 
                              Authentication authentication, 
                              Model model) {
        CustomUserDetailsService.CustomUserPrincipal principal = 
            (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();
        
        Book book = bookService.findById(id);
        
        // Check if user owns this book
        if (book.getAuthorUser() == null || !book.getAuthorUser().getId().equals(user.getId())) {
            return "redirect:/author/books";
        }
        
        model.addAttribute("book", book);
        return "author/book-edit";
    }

    @PostMapping("/books/{id}")
    public String updateBook(@PathVariable Long id,
                           @RequestParam String title,
                           @RequestParam String author,
                           @RequestParam(required = false) String genre,
                           @RequestParam(required = false) String description,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {
        
        CustomUserDetailsService.CustomUserPrincipal principal = 
            (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();
        
        try {
            Book book = bookService.findById(id);
            
            // Check if user owns this book
            if (book.getAuthorUser() == null || !book.getAuthorUser().getId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "Você não tem permissão para editar este livro.");
                return "redirect:/author/books";
            }
            
            book.setTitle(title);
            book.setAuthor(author);
            book.setGenre(genre);
            book.setDescription(description);
            
            bookService.updateBook(book);
            redirectAttributes.addFlashAttribute("success", "Livro atualizado com sucesso!");
            return "redirect:/author/books/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao atualizar livro: " + e.getMessage());
            return "redirect:/author/books/" + id + "/edit";
        }
    }

    @PostMapping("/books/{id}/upload-cover")
    public String uploadCover(@PathVariable Long id,
                             @RequestParam("file") MultipartFile file,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        
        CustomUserDetailsService.CustomUserPrincipal principal = 
            (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();
        
        try {
            Book book = bookService.findById(id);
            
            // Check if user owns this book
            if (book.getAuthorUser() == null || !book.getAuthorUser().getId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "Você não tem permissão para editar este livro.");
                return "redirect:/author/books";
            }
            
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Selecione uma imagem para upload.");
                return "redirect:/author/books/" + id;
            }
            
            // Validate file type
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                redirectAttributes.addFlashAttribute("error", "Apenas arquivos de imagem são permitidos.");
                return "redirect:/author/books/" + id;
            }
            
            // Validate file size (max 5MB)
            if (file.getSize() > 5 * 1024 * 1024) {
                redirectAttributes.addFlashAttribute("error", "A imagem deve ter no máximo 5MB.");
                return "redirect:/author/books/" + id;
            }
            
            bookService.uploadCoverImage(id, file);
            redirectAttributes.addFlashAttribute("success", "Capa do livro atualizada com sucesso!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao fazer upload da imagem: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro inesperado: " + e.getMessage());
        }
        
        return "redirect:/author/books/" + id;
    }
}
