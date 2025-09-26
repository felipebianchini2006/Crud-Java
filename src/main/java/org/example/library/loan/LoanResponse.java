package org.example.library.loan;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record LoanResponse(
        Long id,
        Long bookId,
        String bookTitle,
        Long userId,
        String userName,
        LocalDate loanDate,
        LocalDate dueDate,
        LocalDate returnDate,
        boolean returned
) {
    static LoanResponse from(Loan loan) {
        return new LoanResponse(
                loan.getId(),
                loan.getBook().getId(),
                loan.getBook().getTitle(),
                loan.getUser().getId(),
                loan.getUser().getName(),
                toLocalDate(loan.getLoanDate()),
                toLocalDate(loan.getDueDate()),
                toLocalDate(loan.getReturnDate()),
                loan.isReturned()
        );
    }
    
    private static LocalDate toLocalDate(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toLocalDate() : null;
    }
}