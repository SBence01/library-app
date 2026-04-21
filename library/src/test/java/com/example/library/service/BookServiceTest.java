package com.example.library.service;

import com.example.library.domain.Book;
import com.example.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private UUID bookId;

    @BeforeEach
    void setUp() {
        bookId = UUID.randomUUID();
        book = Book.builder()
                .id(bookId)
                .title("Egri csillagok")
                .author("Gárdonyi Géza")
                .publicationYear(1901)
                .category("Történelmi regény")
                .build();
    }

    @Test
    void findAll_shouldReturnAllBooks() {
        Book book2 = Book.builder()
                .id(UUID.randomUUID())
                .title("A Pál utcai fiúk")
                .author("Molnár Ferenc")
                .build();

        when (bookRepository.findAll()).thenReturn(Arrays.asList(book, book2));

        List<Book> result = bookService.findAll();

        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void findById_shouldReturnBook_whenExists() {
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        Book result = bookService.findById(bookId);

        assertEquals("Egri csillagok", result.getTitle());
        assertEquals("Gárdonyi Géza", result.getAuthor());
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        UUID randomId = UUID.randomUUID();
        when(bookRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookService.findById(randomId));
    }

    @Test
    void save_shouldReturnSavedBook() {
        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.save(book);

        assertEquals("Egri csillagok", result.getTitle());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void update_shouldUpdateAndReturnBook() {
        Book updatedBook = Book.builder()
                .title("Egri csillagok - Javított")
                .author("Gárdonyi Géza")
                .publicationYear(1901)
                .category("Regény")
                .build();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.update(bookId, updatedBook);

        assertNotNull(result);
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void deleteById_shouldDelete_whenExists() {
        when(bookRepository.existsById(bookId)).thenReturn(true);

        bookService.deleteById(bookId);

        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    void deleteById_shouldThrowException_whenNotFound() {
        UUID randomId = UUID.randomUUID();
        when(bookRepository.existsById(randomId)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> bookService.deleteById(randomId));
    }
}
