package org.example.library.web;

import org.example.library.book.BookService;
import org.example.library.loan.LoanService;
import org.example.library.security.CustomUserDetailsService;
import org.example.library.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reader")
public class ReaderWebController {

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
        
        // Get user's active loans
        try {
            model.addAttribute("activeLoans", loanService.findByUser(user.getId()));
        } catch (Exception e) {
            model.addAttribute("activeLoans", java.util.Collections.emptyList());
        }
        
        // Get available books (first 6 for quick access)
        try {
            model.addAttribute("availableBooks", bookService.findAllPaginated(
                org.springframework.data.domain.PageRequest.of(0, 6)).getContent());
        } catch (Exception e) {
            model.addAttribute("availableBooks", java.util.Collections.emptyList());
        }
        
        return "reader/dashboard";
    }
}
