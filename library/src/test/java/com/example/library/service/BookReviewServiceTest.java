package com.example.library.service;

import com.example.library.domain.Book;
import com.example.library.domain.BookReview;
import com.example.library.domain.Customer;
import com.example.library.repository.BookReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookReviewServiceTest {

    @Mock
    private BookReviewRepository bookReviewRepository;

    @Mock
    private BookService bookService;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private BookReviewService bookReviewService;

    private BookReview review;
    private UUID reviewId;
    private Book book;
    private Customer customer;
    private UUID bookId;
    private UUID customerId;

    @BeforeEach
    void setUp() {
        reviewId = UUID.randomUUID();
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

        review = new BookReview();
        review.setId(reviewId);
        review.setBook(book);
        review.setCustomer(customer);
        review.setRating(5);
        review.setComment("Kiváló könyv!");
        review.setReviewDate(LocalDate.now());
    }

    @Test
    void findAll_shouldReturnAllReviews() {
        when(bookReviewRepository.findAll()).thenReturn(Arrays.asList(review));

        List<BookReview> result = bookReviewService.findAll();

        assertEquals(1, result.size());
        verify(bookReviewRepository, times(1)).findAll();
    }

    @Test
    void findById_shouldReturnReview_whenExists() {
        when(bookReviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        BookReview result = bookReviewService.findById(reviewId);

        assertEquals(5, result.getRating());
        assertEquals("Kiváló könyv!", result.getComment());
        verify(bookReviewRepository, times(1)).findById(reviewId);
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        UUID randomId = UUID.randomUUID();
        when(bookReviewRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookReviewService.findById(randomId));
    }

    @Test
    void createReview_shouldCreateAndReturnReview() {
        when(bookService.findById(bookId)).thenReturn(book);
        when(customerService.findById(customerId)).thenReturn(customer);
        when(bookReviewRepository.save(any(BookReview.class))).thenReturn(review);

        BookReview result = bookReviewService.createReview(bookId, customerId, 5, "Kiváló könyv!");

        assertNotNull(result);
        assertEquals(5, result.getRating());
        verify(bookService, times(1)).findById(bookId);
        verify(customerService, times(1)).findById(customerId);
        verify(bookReviewRepository, times(1)).save(any(BookReview.class));
    }

    @Test
    void createReview_shouldThrowException_whenBookNotFound() {
        when(bookService.findById(bookId)).thenThrow(new RuntimeException("Book not found"));

        assertThrows(RuntimeException.class,
                () -> bookReviewService.createReview(bookId, customerId, 5, "Kiváló könyv!"));
    }

    @Test
    void getAverageRating_shouldReturnAverage() {
        BookReview review2 = new BookReview();
        review2.setId(UUID.randomUUID());
        review2.setBook(book);
        review2.setCustomer(customer);
        review2.setRating(3);

        when(bookService.findById(bookId)).thenReturn(book);
        when(bookReviewRepository.findAll()).thenReturn(Arrays.asList(review, review2));

        double average = bookReviewService.getAverageRating(bookId);

        assertEquals(4.0, average);
    }

    @Test
    void getAverageRating_shouldReturnZero_whenNoReviews() {
        when(bookService.findById(bookId)).thenReturn(book);
        when(bookReviewRepository.findAll()).thenReturn(Collections.emptyList());

        double average = bookReviewService.getAverageRating(bookId);

        assertEquals(0.0, average);
    }

    @Test
    void deleteById_shouldDelete_whenExists() {
        when(bookReviewRepository.existsById(reviewId)).thenReturn(true);

        bookReviewService.deleteById(reviewId);

        verify(bookReviewRepository, times(1)).deleteById(reviewId);
    }

    @Test
    void deleteById_shouldThrowException_whenNotFound() {
        UUID randomId = UUID.randomUUID();
        when(bookReviewRepository.existsById(randomId)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> bookReviewService.deleteById(randomId));
    }
}