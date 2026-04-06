package com.example.library.repository;

import com.example.library.domain.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookReviewRepository extends JpaRepository<BookReview, UUID> {
}
