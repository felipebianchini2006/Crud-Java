package org.example.library.loan;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    Optional<Loan> findByBookIdAndReturnDateIsNull(Long bookId);
    List<Loan> findByUserId(Long userId);
    List<Loan> findByBookId(Long bookId);
}