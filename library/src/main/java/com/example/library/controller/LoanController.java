package com.example.library.controller;

import java.util.UUID;

import com.example.library.service.BookService;
import com.example.library.service.CustomerService;
import com.example.library.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;
    private final BookService bookService;
    private final CustomerService customerService;

    @GetMapping
    public String listLoans(Model model) {
        model.addAttribute("loans", loanService.findAll());
        return "loans/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("books", bookService.findAll());
        model.addAttribute("customers", customerService.findAll());
        return "loans/form";
    }

    @PostMapping
    public String createLoan(@RequestParam UUID bookId, @RequestParam UUID customerId) {
        loanService.createLoan(bookId, customerId);
        return "redirect:/loans";
    }

    @GetMapping("/return/{id}")
    public String returnBook(@PathVariable UUID id) {
        loanService.returnBook(id);
        return "redirect:/loans";
    }

    @GetMapping("/delete/{id}")
    public String deleteLoan(@PathVariable UUID id) {
        loanService.deleteById(id);
        return "redirect:/loans";
    }
}
