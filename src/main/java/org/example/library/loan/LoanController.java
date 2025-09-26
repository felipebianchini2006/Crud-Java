package org.example.library.loan;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.example.library.security.CustomUserDetailsService;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService service;

    public LoanController(LoanService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<LoanResponse> create(@Valid @RequestBody LoanRequest request) {
        LoanResponse response = service.create(request);
        return ResponseEntity.created(URI.create("/api/loans/" + response.id())).body(response);
    }

    @GetMapping
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public List<LoanResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public LoanResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/user/{userId}")
    public List<LoanResponse> findByUser(@PathVariable Long userId) {
        return service.findByUser(userId);
    }

    @GetMapping("/me")
    public List<LoanResponse> findForCurrentUser(Authentication authentication) {
        CustomUserDetailsService.CustomUserPrincipal principal =
                (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
        return service.findByUser(principal.getUser().getId());
    }

    @PatchMapping("/{id}/return")
    public LoanResponse returnBook(@PathVariable Long id, @Valid @RequestBody LoanReturnRequest request) {
        return service.returnBook(id, request);
    }

    @DeleteMapping("/{id}")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
