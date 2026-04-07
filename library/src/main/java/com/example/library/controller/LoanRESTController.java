package com.example.library.controller;

import com.example.library.domain.Loan;
import com.example.library.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanRESTController {

    private final LoanService loanService;

    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(loanService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable UUID id) {
        return ResponseEntity.ok(loanService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Loan> createLoan(@RequestParam UUID bookId, @RequestParam UUID customerId) {
        Loan saved = loanService.createLoan(bookId, customerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<Loan> returnBook(@PathVariable UUID id) {
        return ResponseEntity.ok(loanService.returnBook(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable UUID id) {
        loanService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
