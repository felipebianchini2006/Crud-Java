package org.example.library.book;

import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.example.library.common.ResourceNotFoundException;
import org.example.library.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public BookResponse create(BookRequest request) {
        Book book = new Book(request.title(), request.author(), request.genre());
        if (request.available() != null) {
            book.setAvailable(request.available());
        }
        return BookResponse.from(repository.save(book));
    }

    @Transactional(readOnly = true)
    public List<BookResponse> findAll() {
        return repository.findAll().stream()
                .map(BookResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public BookResponse findByIdResponse(Long id) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro", id));
        return BookResponse.from(book);
    }

    @Transactional
    public BookResponse update(Long id, BookRequest request) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro", id));
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setGenre(request.genre());
        if (request.available() != null) {
            book.setAvailable(request.available());
        }
        return BookResponse.from(book);
    }

    @Transactional
    public void delete(Long id) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro", id));
        repository.delete(book);
    }

    @Transactional
    public void setAvailability(Long id, boolean available) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro", id));
        book.setAvailable(available);
    }

    private final String uploadDir = "uploads/covers/";

    // New methods for web controller
    @Transactional(readOnly = true)
    public Book findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro", id));
    }

    @Transactional(readOnly = true)
    public Page<Book> findAllPaginated(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Book> searchBooks(String query, Pageable pageable) {
        return repository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Book> findByGenre(String genre, Pageable pageable) {
        return repository.findByGenreContainingIgnoreCase(genre, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Book> findByAuthor(String author, Pageable pageable) {
        return repository.findByAuthorContainingIgnoreCase(author, pageable);
    }

    @Transactional(readOnly = true)
    public List<String> findAllGenres() {
        return repository.findDistinctGenres();
    }

    @Transactional(readOnly = true)
    public List<String> findAllAuthors() {
        return repository.findDistinctAuthors();
    }

    @Transactional(readOnly = true)
    public List<Book> findByAuthorUser(User authorUser) {
        return repository.findByAuthorUser(authorUser);
    }

    @Transactional
    public Book createBook(String title, String author, String genre, String description, User authorUser) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);
        book.setDescription(description);
        book.setAuthorUser(authorUser);
        book.setAvailable(true);
        return repository.save(book);
    }

    @Transactional
    public String uploadCoverImage(Long bookId, MultipartFile file) throws IOException {
        Book book = findById(bookId);

        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new IOException("Arquivo inválido");
        }
        
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + extension;
        Path filePath = uploadPath.resolve(filename);

        // Save file
        Files.copy(file.getInputStream(), filePath);

        // Update book cover image - corrigir o caminho
        book.setCoverImage("/uploads/covers/" + filename);
        repository.save(book);

        return book.getCoverImage();
    }

    @Transactional
    public Book updateBook(Book book) {
        return repository.save(book);
    }
}