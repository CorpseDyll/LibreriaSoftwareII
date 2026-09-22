package com.libreria.controller;

import com.libreria.domain.Book;
import com.libreria.domain.Review;
import com.libreria.dto.RatingDto;
import com.libreria.dto.ReviewDto;
import com.libreria.search.SearchCriteria;
import com.libreria.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Obtener todos los libros del catálogo.
     */
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    /**
     * Obtener un libro por su ISBN.
     */
    @GetMapping("/{isbn}")
    public ResponseEntity<?> getBookByIsbn(@PathVariable String isbn) {
        return bookService.findByIsbn(isbn)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Libro no encontrado para ISBN: " + isbn)));
    }

    /**
     * Requisito 1: Búsqueda simple por palabra/frase en título y autor.
     * Ejemplo: GET /api/books/search?query=Robert
     */
    @GetMapping("/search")
    public ResponseEntity<List<Book>> simpleSearch(@RequestParam(name = "query", required = false, defaultValue = "") String query) {
        return ResponseEntity.ok(bookService.simpleSearch(query));
    }

    /**
     * Requisito 2: Búsqueda avanzada por combinación de autor, título e ISBN.
     * Ejemplo: POST /api/books/search/advanced  payload: { "author": "Bloch", "title": "Effective" }
     */
    @PostMapping("/search/advanced")
    public ResponseEntity<List<Book>> advancedSearch(@RequestBody SearchCriteria criteria) {
        return ResponseEntity.ok(bookService.advancedSearch(criteria));
    }

    /**
     * Requisito 3: Calificar un libro de 1 a 5 estrellas. No requiere compra previa.
     * Ejemplo: POST /api/books/978-0132350884/ratings  payload: { "userId": "user123", "score": 5 }
     */
    @PostMapping("/{isbn}/ratings")
    public ResponseEntity<?> rateBook(@PathVariable String isbn, @RequestBody RatingDto ratingDto) {
        try {
            Book updatedBook = bookService.rateBook(isbn, ratingDto.getUserId(), ratingDto.getScore());
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Calificación registrada con éxito",
                    "isbn", updatedBook.getIsbn(),
                    "calificacionPromedio", updatedBook.getAverageRating(),
                    "totalValoraciones", updatedBook.getTotalRatingsCount()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Requisito 4a: Previsualizar reseña antes de enviarla.
     * Ejemplo: POST /api/books/978-0132350884/reviews/preview
     */
    @PostMapping("/{isbn}/reviews/preview")
    public ResponseEntity<?> previewReview(@PathVariable String isbn, @RequestBody ReviewDto reviewDto) {
        try {
            Review draft = bookService.createReviewDraft(
                    reviewDto.getReviewerName(),
                    reviewDto.getReviewTitle(),
                    reviewDto.getComment(),
                    reviewDto.getScore()
            );
            String previewText = bookService.previewReview(isbn, draft);
            return ResponseEntity.ok(Map.of(
                    "preview", previewText,
                    "draft", draft
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Requisito 4b: Publicar reseña en el libro. No requiere compra previa.
     * Ejemplo: POST /api/books/978-0132350884/reviews
     */
    @PostMapping("/{isbn}/reviews")
    public ResponseEntity<?> submitReview(@PathVariable String isbn, @RequestBody ReviewDto reviewDto) {
        try {
            Review draft = bookService.createReviewDraft(
                    reviewDto.getReviewerName(),
                    reviewDto.getReviewTitle(),
                    reviewDto.getComment(),
                    reviewDto.getScore()
            );
            Review publishedReview = bookService.submitReview(isbn, draft);
            Map<String, Object> response = new java.util.LinkedHashMap<>();
            response.put("mensaje", "Reseña publicada con éxito en el libro");
            response.put("reviewId", publishedReview.getId() != null ? publishedReview.getId() : 1L);
            response.put("reviewTitle", publishedReview.getReviewTitle());
            response.put("status", publishedReview.getStatus().name());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
