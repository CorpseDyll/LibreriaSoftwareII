package com.libreria.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "books")
public class Book extends Product {

    @Column(name = "isbn", nullable = false, unique = true)
    private String isbn;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "genre")
    private String genre;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "book_ratings", joinColumns = @JoinColumn(name = "book_isbn", referencedColumnName = "id"))
    private List<RatingEmbeddable> ratings = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Review> reviews = new ArrayList<>();

    public Book() {
        super();
    }

    public Book(String id, String isbn, String title, String author, double price, String genre, String description) {
        super(id, title, price, description, ProductType.BOOK);
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("El ISBN del libro no puede estar vacío.");
        }
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("El autor del libro no puede estar vacío.");
        }
        this.isbn = isbn.trim();
        this.title = title.trim();
        this.author = author.trim();
        this.genre = genre != null ? genre.trim() : "General";
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public synchronized void addRating(RatingEmbeddable rating) {
        if (rating != null) {
            this.ratings.add(rating);
        }
    }

    public synchronized void addReview(Review review) {
        if (review != null) {
            if (review.getStatus() != ReviewStatus.SUBMITTED) {
                review.submit();
            }
            review.setBook(this);
            this.reviews.add(review);
            this.ratings.add(new RatingEmbeddable(review.getRatingScore(), review.getReviewerName(), review.getCreatedAt()));
        }
    }

    public List<RatingEmbeddable> getRatings() {
        return Collections.unmodifiableList(ratings);
    }

    public List<Review> getReviews() {
        return Collections.unmodifiableList(reviews);
    }

    public double getAverageRating() {
        if (ratings == null || ratings.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (RatingEmbeddable r : ratings) {
            sum += r.getScore();
        }
        return sum / ratings.size();
    }

    public int getTotalRatingsCount() {
        return ratings != null ? ratings.size() : 0;
    }

    public int getTotalReviewsCount() {
        return reviews != null ? reviews.size() : 0;
    }

    @Override
    public String toString() {
        return String.format("Libro [ISBN: %s, Título: '%s', Autor: '%s', Precio: $%.2f, Calificación Promedio: %.1f/5 (%d valoraciones)]",
                isbn, title, author, getPrice(), getAverageRating(), getTotalRatingsCount());
    }
}
