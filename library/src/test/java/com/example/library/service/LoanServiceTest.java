package com.example.library.service;

import com.example.library.domain.Book;
import com.example.library.domain.Customer;
import com.example.library.domain.Loan;
import com.example.library.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private BookService bookService;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private LoanService loanService;

    private Loan loan;
    private UUID loanId;
    private Book book;
    private Customer customer;
    private UUID bookId;
    private UUID customerId;

    @BeforeEach
    void setUp() {
        loanId = UUID.randomUUID();
        bookId = UUID.randomUUID();
        customerId = UUID.randomUUID();

        book = new Book();
        book.setId(bookId);
        book.setTitle("Egri csillagok");
        book.setAuthor("Gárdonyi Géza");

        customer = new Customer();
        customer.setId(customerId);
        customer.setName("Kiss Péter");
        customer.setEmail("kiss.peter@email.com");

        loan = new Loan();
        loan.setId(loanId);
        loan.setBook(book);
        loan.setCustomer(customer);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(30));
        loan.setReturned(false);
    }

    @Test
    void findAll_shouldReturnAllLoans() {
        when(loanRepository.findAll()).thenReturn(Arrays.asList(loan));

        List<Loan> result = loanService.findAll();

        assertEquals(1, result.size());
        verify(loanRepository, times(1)).findAll();
    }

    @Test
    void findById_shouldReturnLoan_whenExists() {
        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));

        Loan result = loanService.findById(loanId);

        assertEquals(bookId, result.getBook().getId());
        assertEquals(customerId, result.getCustomer().getId());
        verify(loanRepository, times(1)).findById(loanId);
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        UUID randomId = UUID.randomUUID();
        when(loanRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> loanService.findById(randomId));
    }

    @Test
    void createLoan_shouldCreateAndReturnLoan() {
        when(bookService.findById(bookId)).thenReturn(book);
        when(customerService.findById(customerId)).thenReturn(customer);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        Loan result = loanService.createLoan(bookId, customerId);

        assertNotNull(result);
        assertEquals(book, result.getBook());
        assertEquals(customer, result.getCustomer());
        verify(bookService, times(1)).findById(bookId);
        verify(customerService, times(1)).findById(customerId);
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    void createLoan_shouldThrowException_whenBookNotFound() {
        when(bookService.findById(bookId)).thenThrow(new RuntimeException("Book not found"));

        assertThrows(RuntimeException.class, () -> loanService.createLoan(bookId, customerId));
    }

    @Test
    void returnBook_shouldSetReturnedTrue() {
        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        Loan result = loanService.returnBook(loanId);

        assertTrue(result.isReturned());
        assertNotNull(result.getReturnDate());
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    void returnBook_shouldThrowException_whenAlreadyReturned() {
        loan.setReturned(true);
        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));

        assertThrows(RuntimeException.class, () -> loanService.returnBook(loanId));
    }

    @Test
    void deleteById_shouldDelete_whenExists() {
        when(loanRepository.existsById(loanId)).thenReturn(true);

        loanService.deleteById(loanId);

        verify(loanRepository, times(1)).deleteById(loanId);
    }

    @Test
    void deleteById_shouldThrowException_whenNotFound() {
        UUID randomId = UUID.randomUUID();
        when(loanRepository.existsById(randomId)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> loanService.deleteById(randomId));
    }
}