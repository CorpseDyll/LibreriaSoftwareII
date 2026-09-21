package com.libreria.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reviewer_name", nullable = false)
    private String reviewerName;

    @Column(name = "review_title", nullable = false)
    private String reviewTitle;

    @Column(name = "comment", nullable = false, length = 2000)
    private String comment;

    @Column(name = "rating_score", nullable = false)
    private int ratingScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReviewStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_isbn")
    @JsonIgnore
    private Book book;

    public Review() {
        this.createdAt = LocalDateTime.now();
        this.status = ReviewStatus.DRAFT;
    }

    public Review(String reviewerName, String reviewTitle, String comment, int score) {
        if (reviewerName == null || reviewerName.isBlank()) {
            throw new IllegalArgumentException("El nombre del revisor es obligatorio.");
        }
        if (reviewTitle == null || reviewTitle.isBlank()) {
            throw new IllegalArgumentException("El título de la reseña es obligatorio.");
        }
        if (comment == null || comment.isBlank()) {
            throw new IllegalArgumentException("El texto/comentario de la reseña es obligatorio.");
        }
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 (malo) y 5 (bueno). Valor proporcionado: " + score);
        }
        this.reviewerName = reviewerName.trim();
        this.reviewTitle = reviewTitle.trim();
        this.comment = comment.trim();
        this.ratingScore = score;
        this.createdAt = LocalDateTime.now();
        this.status = ReviewStatus.DRAFT;
    }

    public Long getId() {
        return id;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public String getComment() {
        return comment;
    }

    public int getRatingScore() {
        return ratingScore;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ReviewStatus getStatus() {
        return status;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void submit() {
        if (this.status == ReviewStatus.SUBMITTED) {
            throw new IllegalStateException("Esta reseña ya fue publicada previamente.");
        }
        this.status = ReviewStatus.SUBMITTED;
    }

    public String getPreview(String bookTitle, String bookAuthor) {
        StringBuilder sb = new StringBuilder();
        sb.append("=========================================\n");
        sb.append("   VISTA PREVIA DE RESEÑA (PREVIEW)\n");
        sb.append("=========================================\n");
        sb.append("Libro: ").append(bookTitle).append(" por ").append(bookAuthor).append("\n");
        sb.append("Título Reseña: ").append(reviewTitle).append("\n");
        sb.append("Calificación: ").append(ratingScore).append("/5 ★\n");
        sb.append("Por: ").append(reviewerName).append(" [").append(createdAt.toLocalDate()).append("]\n");
        sb.append("Estado: ").append(status.getDescription()).append("\n");
        sb.append("-----------------------------------------\n");
        sb.append(comment).append("\n");
        sb.append("=========================================");
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("Reseña de '%s' [%d★]: %s", reviewerName, ratingScore, reviewTitle);
    }
}
