package com.example.library.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.example.library.domain.Book;
import com.example.library.domain.Customer;
import com.example.library.domain.Loan;
import com.example.library.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookService bookService;
    private final CustomerService customerService;

    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    public Loan findById(UUID id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + id));
    }

    public Loan createLoan(UUID bookId, UUID customerId) {
        Book book = bookService.findById(bookId);
        Customer customer = customerService.findById(customerId);

        Loan loan = Loan.builder()
                .book(book)
                .customer(customer)
                .loanDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(30))
                .returned(false)
                .build();

        return loanRepository.save(loan);
    }

    public Loan returnBook(UUID loanId) {
        Loan loan = findById(loanId);
        if (loan.isReturned()) {
            throw new RuntimeException("This book has already been returned");
        }
        loan.setReturnDate(LocalDate.now());
        loan.setReturned(true);
        return loanRepository.save(loan);
    }

    public void deleteById(UUID id) {
        if (!loanRepository.existsById(id)) {
            throw new RuntimeException("Loan not found with id: " + id);
        }
        loanRepository.deleteById(id);
    }
}
