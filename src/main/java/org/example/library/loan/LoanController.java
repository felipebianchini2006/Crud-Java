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
    public List<LoanResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public LoanResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/reader/{readerId}")
    public List<LoanResponse> findByReader(@PathVariable Long readerId) {
        return service.findByReader(readerId);
    }

    @PatchMapping("/{id}/return")
    public LoanResponse returnBook(@PathVariable Long id, @Valid @RequestBody LoanReturnRequest request) {
        return service.returnBook(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}