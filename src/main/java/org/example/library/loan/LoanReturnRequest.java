package org.example.library.loan;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record LoanReturnRequest(
        @NotNull
        @FutureOrPresent
        LocalDate returnDate
) {
}