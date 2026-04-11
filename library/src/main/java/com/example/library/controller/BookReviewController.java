package com.example.library.controller;

import com.example.library.service.BookReviewService;
import com.example.library.service.BookService;
import com.example.library.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class BookReviewController {

    private final BookReviewService bookReviewService;
    private final BookService bookService;
    private final CustomerService customerService;

    @GetMapping
    public String listReviews(Model model) {
        model.addAttribute("reviews", bookReviewService.findAll());
        return "reviews/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("books", bookService.findAll());
        model.addAttribute("customers", customerService.findAll());
        return "reviews/form";
    }

    @PostMapping
    public String createReview(@RequestParam UUID bookId,
                               @RequestParam UUID customerId,
                               @RequestParam Integer rating,
                               @RequestParam String comment) {
        bookReviewService.createReview(bookId, customerId, rating, comment);
        return "redirect:/reviews";
    }

    @GetMapping("/delete/{id}")
    public String deleteReview(@PathVariable UUID id) {
        bookReviewService.deleteById(id);
        return "redirect:/reviews";
    }
}
