package org.example.library.loan;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record LoanRequest(
        @NotNull Long bookId,
        @NotNull Long readerId,
        @NotNull
        @FutureOrPresent
        LocalDate loanDate,
        @NotNull
        @FutureOrPresent
        LocalDate dueDate
) {
}