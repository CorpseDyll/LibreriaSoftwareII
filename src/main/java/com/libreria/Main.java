package com.libreria;

import com.libreria.domain.Book;
import com.libreria.domain.Product;
import com.libreria.domain.ProductType;
import com.libreria.domain.Review;
import com.libreria.search.SearchCriteria;
import com.libreria.service.BookService;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final BookService bookService = new BookService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  SISTEMA DE LIBRERÍA DE SOFTWARE - FASE 1 (LIBROS)");
        System.out.println("==================================================");

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Seleccione una opción: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> performSimpleSearch();
                case "2" -> performAdvancedSearch();
                case "3" -> rateBook();
                case "4" -> writeAndPreviewReview();
                case "5" -> listAllBooks();
                case "6" -> demonstrateCatalogPolicy();
                case "0" -> {
                    System.out.println("\n¡Gracias por utilizar la Librería de Software!");
                    running = false;
                }
                default -> System.out.println("Opción no válida. Por favor, intente de nuevo.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n---------------- MENU PRINCIPAL ----------------");
        System.out.println("1. Búsqueda simple (Palabra o frase en Autor y Título)");
        System.out.println("2. Búsqueda avanzada (Combinación de Autor, Título e ISBN)");
        System.out.println("3. Calificar un libro (1 a 5 estrellas)");
        System.out.println("4. Escribir y Previsualizar una Reseña de libro");
        System.out.println("5. Listar todos los libros del catálogo");
        System.out.println("6. Demostrar Política de Catálogo (Protección ítems > $10,000 / Solo Libros)");
        System.out.println("0. Salir");
        System.out.println("------------------------------------------------");
    }

    private static void performSimpleSearch() {
        System.out.println("\n--- Búsqueda Simple ---");
        System.out.print("Ingrese palabra o frase a buscar (autor o título): ");
        String query = scanner.nextLine().trim();

        List<Book> results = bookService.simpleSearch(query);
        displaySearchResults(results);
    }

    private static void performAdvancedSearch() {
        System.out.println("\n--- Búsqueda Avanzada ---");
        System.out.print("Título (presione Enter para omitir): ");
        String title = scanner.nextLine().trim();

        System.out.print("Autor (presione Enter para omitir): ");
        String author = scanner.nextLine().trim();

        System.out.print("ISBN (presione Enter para omitir): ");
        String isbn = scanner.nextLine().trim();

        SearchCriteria criteria = SearchCriteria.builder()
                .title(title)
                .author(author)
                .isbn(isbn)
                .build();

        List<Book> results = bookService.advancedSearch(criteria);
        displaySearchResults(results);
    }

    private static void rateBook() {
        System.out.println("\n--- Calificar un Libro ---");
        System.out.print("Ingrese el ISBN del libro: ");
        String isbn = scanner.nextLine().trim();

        System.out.print("Ingrese su nombre o ID de usuario: ");
        String userId = scanner.nextLine().trim();

        System.out.print("Ingrese calificación (1 = malo, 5 = bueno): ");
        try {
            int score = Integer.parseInt(scanner.nextLine().trim());
            bookService.rateBook(isbn, userId, score);
            System.out.println("¡Calificación registrada exitosamente!");
        } catch (NumberFormatException e) {
            System.out.println("Error: La calificación debe ser un número entero.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void writeAndPreviewReview() {
        System.out.println("\n--- Escribir y Previsualizar Reseña ---");
        System.out.print("Ingrese el ISBN del libro: ");
        String isbn = scanner.nextLine().trim();

        Book book;
        try {
            book = bookService.getRepository().findByIsbn(isbn)
                    .orElseThrow(() -> new IllegalArgumentException("No se encontró ningún libro con ISBN: " + isbn));
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        System.out.print("Su nombre: ");
        String name = scanner.nextLine().trim();

        System.out.print("Título de la reseña: ");
        String reviewTitle = scanner.nextLine().trim();

        System.out.print("Comentario / Opinión: ");
        String comment = scanner.nextLine().trim();

        System.out.print("Calificación (1 a 5): ");
        int score;
        try {
            score = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Error: Calificación inválida.");
            return;
        }

        try {
            // 1. Crear Borrador
            Review draft = bookService.createReviewDraft(name, reviewTitle, comment, score);

            // 2. Previsualizar
            System.out.println("\n--- PREVISUALIZACIÓN DE LA RESEÑA ---");
            String previewStr = bookService.previewReview(isbn, draft);
            System.out.println(previewStr);

            // 3. Confirmación de envío
            System.out.print("\n¿Desea publicar esta reseña? (s/n): ");
            String confirm = scanner.nextLine().trim();
            if (confirm.equalsIgnoreCase("s")) {
                bookService.submitReview(isbn, draft);
                System.out.println("¡Reseña publicada con éxito en el catálogo!");
            } else {
                System.out.println("Operación cancelada. La reseña no fue publicada.");
            }
        } catch (Exception e) {
            System.out.println("Error al procesar la reseña: " + e.getMessage());
        }
    }

    private static void listAllBooks() {
        System.out.println("\n--- Catálogo de Libros Disponible ---");
        List<Book> books = bookService.getAllBooks();
        displaySearchResults(books);
    }

    private static void demonstrateCatalogPolicy() {
        System.out.println("\n--- Demostración de Política de Catálogo (Planned Change) ---");
        System.out.println("Regla actual: Solo se vende LIBROS y se bloquean ítems > $10,000 para mitigar riesgos.");

        // Intento 1: Registrar libro normal
        Book normalBook = new Book("B999", "978-0000000000", "Refactoring", "Martin Fowler", 47.99, "Software", "Clean refactoring guide");
        try {
            bookService.registerProduct(normalBook);
            System.out.println("✔ Éxito al registrar libro dentro de políticas: " + normalBook.getName());
        } catch (Exception e) {
            System.out.println("✖ Error: " + e.getMessage());
        }

        // Intento 2: Registrar libro de lujo de $15,000
        Book luxuryBook = new Book("B888", "978-9999999999", "Manuscrito Antiguo de Colección", "Coleccionista", 15000.00, "Rare", "Super expensive book");
        try {
            bookService.registerProduct(luxuryBook);
        } catch (Exception e) {
            System.out.println("✔ Intención de bloqueo exitosa: " + e.getMessage());
        }

        // Intento 3: Registrar un TV / Electrónica
        Product electronics = new Product("E100", "Televisor OLED 4K", 2500.00, "Smart TV OLED", ProductType.ELECTRONICS) {};
        try {
            bookService.registerProduct(electronics);
        } catch (Exception e) {
            System.out.println("✔ Intención de bloqueo exitosa: " + e.getMessage());
        }
    }

    private static void displaySearchResults(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("No se encontraron libros que coincidan con la búsqueda.");
            return;
        }

        System.out.println("\nResultados encontrados (" + books.size() + "):");
        for (int i = 0; i < books.size(); i++) {
            Book b = books.get(i);
            System.out.printf("%d. [ISBN: %s] %s - %s ($%.2f) | Calificación: %.1f★ (%d opiniones)\n",
                    (i + 1), b.getIsbn(), b.getTitle(), b.getAuthor(), b.getPrice(), b.getAverageRating(), b.getTotalRatingsCount());
            if (!b.getReviews().isEmpty()) {
                System.out.println("   Última reseña: \"" + b.getReviews().get(b.getReviews().size() - 1).getReviewTitle() + "\"");
            }
        }
    }
}
