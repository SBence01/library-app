package com.example.library.service;

import com.example.library.domain.Book;
import com.example.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(UUID id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public Book update(UUID id, Book updatedBook) {
        Book existing = findById(id);
        existing.setTitle(updatedBook.getTitle());
        existing.setAuthor(updatedBook.getAuthor());
        existing.setPublicationYear(updatedBook.getPublicationYear());
        existing.setCategory(updatedBook.getCategory());
        return bookRepository.save(existing);
    }

    public void deleteById(UUID id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }
}
