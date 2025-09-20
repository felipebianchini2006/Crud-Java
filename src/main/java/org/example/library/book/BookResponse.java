package org.example.library.book;

public record BookResponse(
        Long id,
        String title,
        String author,
        String genre,
        boolean available
) {
    static BookResponse from(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.isAvailable()
        );
    }
}