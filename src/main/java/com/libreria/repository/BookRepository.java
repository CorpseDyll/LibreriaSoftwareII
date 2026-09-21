package com.libreria.repository;

import com.libreria.domain.Book;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class BookRepository {
    private final Map<String, Book> store = new ConcurrentHashMap<>();

    public BookRepository() {
        initSampleData();
    }

    public void save(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("El libro no puede ser nulo.");
        }
        store.put(book.getIsbn(), book);
    }

    public Optional<Book> findByIsbn(String isbn) {
        if (isbn == null || isbn.isBlank()) {
            return Optional.empty();
        }
        String cleanIsbn = isbn.trim();
        return Optional.ofNullable(store.get(cleanIsbn));
    }

    public List<Book> findAll() {
        return new ArrayList<>(store.values());
    }

    public boolean existsByIsbn(String isbn) {
        return findByIsbn(isbn).isPresent();
    }

    public void clear() {
        store.clear();
    }

    private void initSampleData() {
        Book b1 = new Book("B001", "978-0132350884", "Clean Code: A Handbook of Agile Software Craftsmanship",
                "Robert C. Martin", 42.50, "Software",
                "Un manual fundamental sobre artesanía de código limpio.");
        Book b2 = new Book("B002", "978-0201633610", "Design Patterns: Elements of Reusable Object-Oriented Software",
                "Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides", 54.99, "Software",
                "El libro clásico de la Banda de los Cuatro (GoF) sobre patrones de diseño.");
        Book b3 = new Book("B003", "978-0134685991", "Effective Java",
                "Joshua Bloch", 49.99, "Software",
                "Las mejores prácticas para la plataforma de programación Java.");
        Book b4 = new Book("B004", "978-0596007126", "Head First Design Patterns",
                "Eric Freeman, Elisabeth Robson", 38.00, "Software",
                "Una guía de diseño orientada a objetos fascinante y visual.");
        Book b5 = new Book("B005", "978-0321127426", "Patterns of Enterprise Application Architecture",
                "Martin Fowler", 59.99, "Software",
                "Patrones clave para la arquitectura de aplicaciones empresariales.");

        save(b1);
        save(b2);
        save(b3);
        save(b4);
        save(b5);
    }
}
