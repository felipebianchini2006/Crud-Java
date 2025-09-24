package org.example.library.web;

import org.example.library.book.Book;
import org.example.library.book.BookService;
import org.example.library.loan.LoanRequest;
import org.example.library.loan.LoanService;
import org.example.library.security.CustomUserDetailsService;
import org.example.library.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/loans")
public class LoanWebController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private BookService bookService;

    @PostMapping("/borrow/{bookId}")
    public String borrowBook(@PathVariable Long bookId,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {
        
        CustomUserDetailsService.CustomUserPrincipal principal = 
            (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();
        
        try {
            Book book = bookService.findById(bookId);
            
            if (!book.isAvailable()) {
                redirectAttributes.addFlashAttribute("error", "Este livro não está disponível para empréstimo.");
                return "redirect:/books/" + bookId;
            }
            
            // Create loan for 14 days
            LocalDate loanDate = LocalDate.now();
            LocalDate dueDate = loanDate.plusDays(14);
            
            // Ordem correta dos parâmetros: bookId, readerId, loanDate, dueDate
            LoanRequest loanRequest = new LoanRequest(bookId, user.getId(), loanDate, dueDate);
            loanService.create(loanRequest);
            
            redirectAttributes.addFlashAttribute("success", "Livro emprestado com sucesso! Devolução até: " + dueDate);
            return "redirect:/reader/dashboard";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao emprestar livro: " + e.getMessage());
            return "redirect:/books/" + bookId;
        }
    }

    @PostMapping("/return/{loanId}")
    public String returnBook(@PathVariable Long loanId,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {
        
        try {
            loanService.returnLoan(loanId);
            redirectAttributes.addFlashAttribute("success", "Livro devolvido com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao devolver livro: " + e.getMessage());
        }
        
        return "redirect:/reader/dashboard";
    }

    @GetMapping("/my-loans")
    public String myLoans(Authentication authentication, Model model) {
        CustomUserDetailsService.CustomUserPrincipal principal = 
            (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();
        
        model.addAttribute("user", user);
        model.addAttribute("loans", loanService.findByUser(user.getId()));
        
        return "reader/my-loans";
    }
}
