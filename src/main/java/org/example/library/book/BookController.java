package org.example.library.book;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import org.example.library.security.CustomUserDetailsService;
import org.example.library.user.User;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @PostMapping
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    public ResponseEntity<BookResponse> create(@Valid @RequestBody BookRequest request) {
        BookResponse response = service.create(request);
        return ResponseEntity
                .created(URI.create("/api/books/" + response.id()))
                .body(response);
    }

    @GetMapping
    public List<BookResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public BookResponse findById(@PathVariable Long id) {
        return service.findByIdResponse(id);
    }

    @PutMapping("/{id}")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    public BookResponse update(@PathVariable Long id, @Valid @RequestBody BookRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/availability")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    public ResponseEntity<Void> changeAvailability(@PathVariable Long id, @RequestParam boolean available) {
        service.setAvailability(id, available);
        return ResponseEntity.noContent().build();
    }

    // Upload de capa do livro
    @PostMapping("/{id}/cover")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    public BookResponse uploadCover(@PathVariable Long id,
                                    @RequestParam("file") MultipartFile file) throws java.io.IOException {
        service.uploadCoverImage(id, file);
        return service.findByIdResponse(id);
    }

    // Livros do autor logado
    @GetMapping("/mine")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    public List<BookResponse> myBooks(Authentication authentication) {
        CustomUserDetailsService.CustomUserPrincipal principal =
                (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();
        return service.findByAuthorUser(user).stream().map(BookResponse::from).toList();
    }

    // Paginated endpoint for SPA
    @GetMapping("/page")
    public org.springframework.data.domain.Page<BookResponse> page(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String author) {
        var pageable = org.springframework.data.domain.PageRequest.of(page, size);
        org.springframework.data.domain.Page<org.example.library.book.Book> books;
        if (search != null && !search.isBlank()) {
            books = service.searchBooks(search, pageable);
        } else if (genre != null && !genre.isBlank()) {
            books = service.findByGenre(genre, pageable);
        } else if (author != null && !author.isBlank()) {
            books = service.findByAuthor(author, pageable);
        } else {
            books = service.findAllPaginated(pageable);
        }
        return books.map(BookResponse::from);
    }
}
