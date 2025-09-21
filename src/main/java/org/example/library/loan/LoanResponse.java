package org.example.library.loan;

import java.time.LocalDate;

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
                loan.getLoanDate(),
                loan.getDueDate(),
                loan.getReturnDate(),
                loan.isReturned()
        );
    }
}