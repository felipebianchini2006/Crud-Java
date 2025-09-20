package org.example.library.reader;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/readers")
public class ReaderController {

    private final ReaderService service;

    public ReaderController(ReaderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ReaderResponse> create(@Valid @RequestBody ReaderRequest request) {
        ReaderResponse response = service.create(request);
        return ResponseEntity.created(URI.create("/api/readers/" + response.id())).body(response);
    }

    @GetMapping
    public List<ReaderResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ReaderResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public ReaderResponse update(@PathVariable Long id, @Valid @RequestBody ReaderRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}