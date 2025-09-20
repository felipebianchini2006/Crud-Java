package org.example.library.book;

import java.util.List;
import org.example.library.common.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public BookResponse findById(Long id) {
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
}