package org.example.library.loan;

import java.time.LocalDateTime;
import java.util.List;
import org.example.library.book.Book;
import org.example.library.book.BookRepository;
import org.example.library.common.BusinessException;
import org.example.library.common.ResourceNotFoundException;
import org.example.library.user.User;
import org.example.library.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public LoanResponse create(LoanRequest request) {
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new ResourceNotFoundException("Livro", request.bookId()));
        if (!book.isAvailable()) {
            throw new BusinessException("Livro não está disponível para empréstimo.");
        }
        User user = userRepository.findById(request.readerId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", request.readerId()));
        if (request.dueDate().isBefore(request.loanDate())) {
            throw new BusinessException("Data de devolução deve ser posterior ou igual à data de empréstimo.");
        }
        loanRepository.findByBookIdAndReturnDateIsNull(book.getId())
                .ifPresent(activeLoan -> {
                    throw new BusinessException("Este livro já está emprestado.");
                });
        LocalDateTime loanDateTime = request.loanDate().atStartOfDay();
        LocalDateTime dueDateTime = request.dueDate().atStartOfDay();
        Loan loan = new Loan(user, book, loanDateTime, dueDateTime);
        book.setAvailable(false);
        return LoanResponse.from(loanRepository.save(loan));
    }

    @Transactional(readOnly = true)
    public List<LoanResponse> findAll() {
        return loanRepository.findAll().stream()
                .map(LoanResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public LoanResponse findById(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empréstimo", id));
        return LoanResponse.from(loan);
    }

    @Transactional(readOnly = true)
    public List<LoanResponse> findByUser(Long userId) {
        return loanRepository.findByUserId(userId).stream()
                .map(LoanResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LoanResponse> findByBook(Long bookId) {
        return loanRepository.findByBookId(bookId).stream()
                .map(LoanResponse::from)
                .toList();
    }

    @Transactional
    public LoanResponse returnBook(Long id, LoanReturnRequest request) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empréstimo", id));
        if (loan.isReturned()) {
            throw new BusinessException("Empréstimo já devolvido.");
        }
        LocalDateTime returnDateTime = request.returnDate().atStartOfDay();
        if (returnDateTime.isBefore(loan.getLoanDate())) {
            throw new BusinessException("Data de devolução não pode ser anterior à data do empréstimo.");
        }
        loan.setReturnDate(returnDateTime);
        loan.getBook().setAvailable(true);
        return LoanResponse.from(loan);
    }

    @Transactional
    public void delete(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empréstimo", id));
        loanRepository.delete(loan);
    }

    @Transactional
    public void returnLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Empréstimo", loanId));
        
        if (loan.isReturned()) {
            throw new BusinessException("Empréstimo já devolvido.");
        }
        
        loan.setReturnDate(LocalDateTime.now());
        loan.getBook().setAvailable(true);
        loanRepository.save(loan);
    }
}