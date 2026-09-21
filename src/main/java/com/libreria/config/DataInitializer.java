package com.libreria.config;

import com.libreria.domain.Book;
import com.libreria.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final BookRepository repository;

    public DataInitializer(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
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

            repository.save(b1);
            repository.save(b2);
            repository.save(b3);
            repository.save(b4);
            repository.save(b5);
            System.out.println(">>> Base de datos inicializada con 5 libros de ejemplo en Spring Boot.");
        }
    }
}
