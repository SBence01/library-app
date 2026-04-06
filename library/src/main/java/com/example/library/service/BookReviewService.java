package com.example.library.service;

import com.example.library.domain.Book;
import com.example.library.domain.BookReview;
import com.example.library.domain.Customer;
import com.example.library.repository.BookReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookReviewService {

    private final BookReviewRepository bookReviewRepository;
    private final BookService bookService;
    private final CustomerService customerService;

    public List<BookReview> findAll() {
        return bookReviewRepository.findAll();
    }

    public BookReview findById(UUID id) {
        return bookReviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
    }

    public BookReview createReview(UUID bookId, UUID customerId, Integer rating, String comment) {
        Book book = bookService.findById(bookId);
        Customer customer = customerService.findById(customerId);

        BookReview review = BookReview.builder()
                .book(book)
                .customer(customer)
                .rating(rating)
                .comment(comment)
                .reviewDate(LocalDate.now())
                .build();

        return bookReviewRepository.save(review);
    }

    public double getAverageRating(UUID bookId) {
        bookService.findById(bookId);
        List<BookReview> reviews = bookReviewRepository.findAll().stream()
                .filter(r -> r.getBook().getId().equals(bookId))
                .toList();

        if (reviews.isEmpty()) {
            return 0.0;
        }

        return reviews.stream()
                .mapToInt(BookReview::getRating)
                .average()
                .orElse(0.0);
    }

    public void deleteById(UUID id) {
        if (!bookReviewRepository.existsById(id)) {
            throw new RuntimeException("Review not found with id: " + id);
        }
        bookReviewRepository.deleteById(id);
    }
}
