package com.example.library.controller;

import java.util.List;
import java.util.UUID;

import com.example.library.domain.BookReview;
import com.example.library.service.BookReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class BookReviewRESTController {

    private final BookReviewService bookReviewService;

    @GetMapping
    public ResponseEntity<List<BookReview>> getAllReviews() {
        return ResponseEntity.ok(bookReviewService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookReview> getReviewById(@PathVariable UUID id) {
        return ResponseEntity.ok(bookReviewService.findById(id));
    }

    @PostMapping
    public ResponseEntity<BookReview> createReview(@RequestParam UUID bookId,
                                                   @RequestParam UUID customerId,
                                                   @RequestParam Integer rating,
                                                   @RequestParam String comment) {
        BookReview saved = bookReviewService.createReview(bookId, customerId, rating, comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/book/{bookId}/average")
    public ResponseEntity<Double> getAverageRating(@PathVariable UUID bookId) {
        return ResponseEntity.ok(bookReviewService.getAverageRating(bookId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID id) {
        bookReviewService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
