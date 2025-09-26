package org.example.library.book;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.library.common.BaseEntity;
import org.example.library.user.User;
import org.example.library.loan.Loan;

import java.util.List;

@Entity
@Table(name = "books")
public class Book extends BaseEntity {

    @NotBlank(message = "Título é obrigatório")
    @Size(max = 200, message = "Título não pode ter mais de 200 caracteres")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Autor é obrigatório")
    @Size(max = 100, message = "Autor não pode ter mais de 100 caracteres")
    @Column(nullable = false)
    private String author;

    @NotBlank(message = "ISBN é obrigatório")
    @Size(max = 20, message = "ISBN não pode ter mais de 20 caracteres")
    @Column(nullable = false, unique = true)
    private String isbn;

    @Size(max = 80, message = "Gênero não pode ter mais de 80 caracteres")
    @Column(length = 80)
    private String genre;

    @Size(max = 1000, message = "Descrição não pode ter mais de 1000 caracteres")
    @Column(length = 1000)
    private String description;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @NotNull(message = "Cópias disponíveis é obrigatório")
    @Column(nullable = false)
    private Integer availableCopies = 0;

    @NotNull(message = "Total de cópias é obrigatório")
    @Column(nullable = false)
    private Integer totalCopies = 0;

    // Relacionamentos
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Loan> loans;

    // Constructors
    public Book() {}

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public Integer getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(Integer availableCopies) {
        this.availableCopies = availableCopies;
    }

    public Integer getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(Integer totalCopies) {
        this.totalCopies = totalCopies;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }

    // Business methods
    public boolean isAvailable() {
        return availableCopies != null && availableCopies > 0;
    }

    public void setAvailable(Boolean available) {
        // This method is for backward compatibility
        // In reality, availability is calculated based on availableCopies
        if (available != null && !available) {
            this.availableCopies = 0;
        } else if (available != null && available && this.availableCopies == 0) {
            this.availableCopies = Math.max(1, this.totalCopies);
        }
    }

    public void setAvailable(boolean available) {
        setAvailable(Boolean.valueOf(available));
    }

    public void setAuthorUser(User authorUser) {
        this.createdBy = authorUser;
    }

    public void setCoverImage(String coverImage) {
        this.coverImageUrl = coverImage;
    }

    public String getCoverImage() {
        return this.coverImageUrl;
    }

    public void borrowCopy() {
        if (isAvailable()) {
            this.availableCopies--;
        } else {
            throw new IllegalStateException("Não há cópias disponíveis para empréstimo");
        }
    }

    public void returnCopy() {
        if (availableCopies < totalCopies) {
            this.availableCopies++;
        } else {
            throw new IllegalStateException("Não é possível devolver mais cópias que o total");
        }
    }
}