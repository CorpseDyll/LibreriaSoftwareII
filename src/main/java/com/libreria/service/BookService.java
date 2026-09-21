package com.libreria.service;

import com.libreria.domain.*;
import com.libreria.repository.BookRepository;
import com.libreria.search.SearchCriteria;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookService {

    private final BookRepository repository;
    private final CatalogPolicy catalogPolicy;

    public BookService(BookRepository repository, CatalogPolicy catalogPolicy) {
        this.repository = repository;
        this.catalogPolicy = catalogPolicy;
    }

    public CatalogPolicy getCatalogPolicy() {
        return catalogPolicy;
    }

    public BookRepository getRepository() {
        return repository;
    }

    /**
     * Registro de producto aplicando la política actual del catálogo (Planned Change).
     */
    public Book registerProduct(Product product) {
        if (!catalogPolicy.isProductAllowedForSale(product)) {
            throw new IllegalArgumentException(catalogPolicy.getRejectionReason(product));
        }

        if (product instanceof Book book) {
            return repository.save(book);
        } else {
            throw new UnsupportedOperationException("El tipo de producto está permitido por política, pero aún no hay repositorio implementado para non-books.");
        }
    }

    /**
     * Requisito 1: "A user can do a basic simple search that searches for a word or phrase in both the author and title fields."
     */
    @Transactional(readOnly = true)
    public List<Book> simpleSearch(String query) {
        if (query == null || query.isBlank()) {
            return repository.findAll();
        }
        String cleanQuery = query.trim();
        return repository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(cleanQuery, cleanQuery);
    }

    /**
     * Requisito 2: "A user can search for books by entering values in any combination of author, title and ISBN"
     */
    @Transactional(readOnly = true)
    public List<Book> advancedSearch(SearchCriteria criteria) {
        if (criteria == null || criteria.isEmpty()) {
            return repository.findAll();
        }

        Specification<Book> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!criteria.getAuthor().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("author")), "%" + criteria.getAuthor().toLowerCase() + "%"));
            }
            if (!criteria.getTitle().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + criteria.getTitle().toLowerCase() + "%"));
            }
            if (!criteria.getIsbn().isEmpty()) {
                String queryIsbn = criteria.getIsbn().trim();
                String cleanQueryIsbn = queryIsbn.replace("-", "").toLowerCase();
                
                jakarta.persistence.criteria.Expression<String> cleanDbIsbn = cb.function("replace", String.class, root.get("isbn"), cb.literal("-"), cb.literal(""));
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("isbn")), "%" + queryIsbn.toLowerCase() + "%"),
                        cb.like(cb.lower(cleanDbIsbn), "%" + cleanQueryIsbn + "%")
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return repository.findAll(spec);
    }

    /**
     * Requisito 3: "A user can rate books from 1 (bad) to 5 (good). The book does not have to be one the user bought from us"
     */
    public Book rateBook(String isbn, String userId, int score) {
        Book book = repository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró ningún libro con ISBN: " + isbn));

        RatingEmbeddable rating = new RatingEmbeddable(score, userId);
        book.addRating(rating);
        return repository.save(book);
    }

    /**
     * Requisito 4a: Creación del borrador de reseña.
     */
    public Review createReviewDraft(String reviewerName, String title, String comment, int score) {
        return new Review(reviewerName, title, comment, score);
    }

    /**
     * Requisito 4b: Previsualización de la reseña antes de ser enviada.
     */
    @Transactional(readOnly = true)
    public String previewReview(String isbn, Review reviewDraft) {
        Book book = repository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró ningún libro con ISBN: " + isbn));
        return reviewDraft.getPreview(book.getTitle(), book.getAuthor());
    }

    /**
     * Requisito 4c: "A user can write a review of a book. She can preview the review before submitting it.
     * The book does not have to be one the user bought from us"
     */
    public Review submitReview(String isbn, Review reviewDraft) {
        Book book = repository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró ningún libro con ISBN: " + isbn));

        book.addReview(reviewDraft);
        repository.save(book);
        return reviewDraft;
    }

    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Book> findByIsbn(String isbn) {
        return repository.findByIsbn(isbn);
    }
}
