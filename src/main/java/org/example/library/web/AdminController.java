package org.example.library.web;

import org.example.library.book.BookService;
import org.example.library.loan.LoanService;
import org.example.library.security.CustomUserDetailsService;
import org.example.library.user.User;
import org.example.library.user.UserRole;
import org.example.library.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private LoanService loanService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        CustomUserDetailsService.CustomUserPrincipal principal = 
            (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();
        
        model.addAttribute("user", user);
        
        // Get statistics
        try {
            var allBooks = bookService.findAllPaginated(PageRequest.of(0, Integer.MAX_VALUE));
            var allUsers = userService.findAll();
            var allLoans = loanService.findAll();
            
            model.addAttribute("totalBooks", allBooks.getTotalElements());
            model.addAttribute("totalUsers", allUsers.size());
            model.addAttribute("totalLoans", allLoans.size());
            model.addAttribute("activeLoans", allLoans.stream().filter(loan -> !loan.returned()).count());
            
            // Get recent books and users
            model.addAttribute("recentBooks", allBooks.getContent().stream().limit(5).toList());
            model.addAttribute("recentUsers", allUsers.stream().limit(5).toList());
            
            // Get users by role
            model.addAttribute("readers", userService.findByRole(UserRole.READER));
            model.addAttribute("authors", userService.findByRole(UserRole.AUTHOR));
            model.addAttribute("admins", userService.findByRole(UserRole.ADMIN));
            
        } catch (Exception e) {
            model.addAttribute("totalBooks", 0);
            model.addAttribute("totalUsers", 0);
            model.addAttribute("totalLoans", 0);
            model.addAttribute("activeLoans", 0);
            model.addAttribute("recentBooks", java.util.Collections.emptyList());
            model.addAttribute("recentUsers", java.util.Collections.emptyList());
            model.addAttribute("readers", java.util.Collections.emptyList());
            model.addAttribute("authors", java.util.Collections.emptyList());
            model.addAttribute("admins", java.util.Collections.emptyList());
        }
        
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @GetMapping("/books")
    public String manageBooks(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "20") int size,
                             Model model) {
        var books = bookService.findAllPaginated(PageRequest.of(page, size));
        model.addAttribute("books", books);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", books.getTotalPages());
        return "admin/books";
    }

    @GetMapping("/loans")
    public String manageLoans(Model model) {
        model.addAttribute("loans", loanService.findAll());
        return "admin/loans";
    }

    @PostMapping("/users/{id}/toggle-status")
    public String toggleUserStatus(@PathVariable Long id, 
                                  RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findById(id);
            if (user.isActive()) {
                userService.delete(id); // This sets active to false
                redirectAttributes.addFlashAttribute("success", "Usuário desativado com sucesso!");
            } else {
                userService.activate(id);
                redirectAttributes.addFlashAttribute("success", "Usuário ativado com sucesso!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao alterar status do usuário: " + e.getMessage());
        }
        
        return "redirect:/admin/users";
    }

    @PostMapping("/books/{id}/delete")
    public String deleteBook(@PathVariable Long id, 
                            RedirectAttributes redirectAttributes) {
        try {
            bookService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Livro excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao excluir livro: " + e.getMessage());
        }
        
        return "redirect:/admin/books";
    }

    @PostMapping("/loans/{id}/force-return")
    public String forceReturnLoan(@PathVariable Long id, 
                                 RedirectAttributes redirectAttributes) {
        try {
            // Force return the loan with current date
            var returnRequest = new org.example.library.loan.LoanReturnRequest(
                java.time.LocalDate.now()
            );
            loanService.returnBook(id, returnRequest);
            redirectAttributes.addFlashAttribute("success", "Devolução forçada realizada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao forçar devolução: " + e.getMessage());
        }
        
        return "redirect:/admin/loans";
    }

    @GetMapping("/reports")
    public String reports(Model model) {
        // TODO: Implement reports functionality
        return "admin/reports";
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        // TODO: Implement system settings
        return "admin/settings";
    }
}
