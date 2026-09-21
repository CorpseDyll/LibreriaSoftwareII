package com.libreria.domain;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

public class Book extends Product {
    private final String isbn;
    private final String title;
    private final String author;
    private final String genre;
    private final List<Rating> ratings;
    private final List<Review> reviews;

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
        this.ratings = new ArrayList<>();
        this.reviews = new ArrayList<>();
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

    public synchronized void addRating(Rating rating) {
        if (rating != null) {
            this.ratings.add(rating);
        }
    }

    public synchronized void addReview(Review review) {
        if (review != null) {
            if (review.getStatus() != ReviewStatus.SUBMITTED) {
                review.submit();
            }
            this.reviews.add(review);
            // Also automatically append review's rating to ratings list if not already included
            this.ratings.add(review.getRating());
        }
    }

    public List<Rating> getRatings() {
        return Collections.unmodifiableList(ratings);
    }

    public List<Review> getReviews() {
        return Collections.unmodifiableList(reviews);
    }

    public double getAverageRating() {
        if (ratings.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (Rating r : ratings) {
            sum += r.getScore();
        }
        return sum / ratings.size();
    }

    public int getTotalRatingsCount() {
        return ratings.size();
    }

    public int getTotalReviewsCount() {
        return reviews.size();
    }

    @Override
    public String toString() {
        return String.format("Libro [ISBN: %s, Título: '%s', Autor: '%s', Precio: $%.2f, Calificación Promedio: %.1f/5 (%d valoraciones)]",
                isbn, title, author, getPrice(), getAverageRating(), getTotalRatingsCount());
    }
}
