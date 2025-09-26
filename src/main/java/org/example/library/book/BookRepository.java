package org.example.library.book;

import org.example.library.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    Page<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author, Pageable pageable);
    
    Page<Book> findByGenreContainingIgnoreCase(String genre, Pageable pageable);
    
    Page<Book> findByAuthorContainingIgnoreCase(String author, Pageable pageable);
    
    List<Book> findByCreatedBy(User createdBy);
    
    @Query("SELECT DISTINCT b.genre FROM Book b WHERE b.genre IS NOT NULL ORDER BY b.genre")
    List<String> findDistinctGenres();
    
    @Query("SELECT DISTINCT b.author FROM Book b WHERE b.author IS NOT NULL ORDER BY b.author")
    List<String> findDistinctAuthors();
}