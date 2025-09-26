package org.example.library.loan;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.example.library.common.BaseEntity;
import org.example.library.book.Book;
import org.example.library.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
public class Loan extends BaseEntity {

    @NotNull(message = "Usuário é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Livro é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @NotNull(message = "Data de empréstimo é obrigatória")
    @Column(name = "loan_date", nullable = false)
    private LocalDateTime loanDate;

    @NotNull(message = "Data de devolução prevista é obrigatória")
    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status = LoanStatus.ACTIVE;

    @Column(length = 500)
    private String notes;

    // Constructors
    public Loan() {}

    public Loan(User user, Book book, LocalDateTime loanDate, LocalDateTime dueDate) {
        this.user = user;
        this.book = book;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
    }

    // Getters and Setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDateTime getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDateTime loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Business methods
    public boolean isOverdue() {
        return status == LoanStatus.ACTIVE && LocalDateTime.now().isAfter(dueDate);
    }

    public boolean isActive() {
        return status == LoanStatus.ACTIVE;
    }

    public boolean isReturned() {
        return status == LoanStatus.RETURNED;
    }

    public void returnBook() {
        if (status != LoanStatus.ACTIVE) {
            throw new IllegalStateException("Empréstimo não está ativo");
        }
        this.returnDate = LocalDateTime.now();
        this.status = LoanStatus.RETURNED;
    }

    public long getDaysUntilDue() {
        if (status != LoanStatus.ACTIVE) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDateTime.now(), dueDate);
    }

    public long getDaysOverdue() {
        if (!isOverdue()) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(dueDate, LocalDateTime.now());
    }
}