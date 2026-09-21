package com.libreria.service;

import com.libreria.domain.*;
import com.libreria.repository.BookRepository;
import com.libreria.search.*;

import java.util.List;

public class BookService {
    private final BookRepository repository;
    private final CatalogPolicy catalogPolicy;

    public BookService(BookRepository repository, CatalogPolicy catalogPolicy) {
        this.repository = repository;
        this.catalogPolicy = catalogPolicy;
    }

    public BookService() {
        this.repository = new BookRepository();
        this.catalogPolicy = new CatalogPolicy();
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
    public void registerProduct(Product product) {
        if (!catalogPolicy.isProductAllowedForSale(product)) {
            throw new IllegalArgumentException(catalogPolicy.getRejectionReason(product));
        }

        if (product instanceof Book book) {
            repository.save(book);
        } else {
            throw new UnsupportedOperationException("El tipo de producto está permitido por política, pero aún no hay repositorio implementado para non-books.");
        }
    }

    /**
     * Requisito 1: "A user can do a basic simple search that searches for a word or phrase in both the author and title fields."
     */
    public List<Book> simpleSearch(String query) {
        SearchStrategy strategy = new SimpleSearchStrategy(query);
        return strategy.search(repository.findAll());
    }

    /**
     * Requisito 2: "A user can search for books by entering values in any combination of author, title and ISBN"
     */
    public List<Book> advancedSearch(SearchCriteria criteria) {
        SearchStrategy strategy = new AdvancedSearchStrategy(criteria);
        return strategy.search(repository.findAll());
    }

    /**
     * Requisito 3: "A user can rate books from 1 (bad) to 5 (good). The book does not have to be one the user bought from us"
     */
    public void rateBook(String isbn, String userId, int score) {
        Book book = repository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró ningún libro con ISBN: " + isbn));

        Rating rating = new Rating(score, userId);
        book.addRating(rating);
        repository.save(book);
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
    public String previewReview(String isbn, Review reviewDraft) {
        Book book = repository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró ningún libro con ISBN: " + isbn));
        return reviewDraft.getPreview(book.getTitle(), book.getAuthor());
    }

    /**
     * Requisito 4c: "A user can write a review of a book. She can preview the review before submitting it.
     * The book does not have to be one the user bought from us"
     */
    public void submitReview(String isbn, Review reviewDraft) {
        Book book = repository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró ningún libro con ISBN: " + isbn));

        book.addReview(reviewDraft);
        repository.save(book);
    }

    public List<Book> getAllBooks() {
        return repository.findAll();
    }
}
