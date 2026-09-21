package com.libreria.domain;

import java.time.LocalDateTime;

public class Review {
    private final String reviewerName;
    private final String reviewTitle;
    private final String comment;
    private final Rating rating;
    private final LocalDateTime createdAt;
    private ReviewStatus status;

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
        this.reviewerName = reviewerName;
        this.reviewTitle = reviewTitle;
        this.comment = comment;
        this.rating = new Rating(score, reviewerName);
        this.createdAt = LocalDateTime.now();
        this.status = ReviewStatus.DRAFT;
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

    public Rating getRating() {
        return rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ReviewStatus getStatus() {
        return status;
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
        sb.append("Calificación: ").append(rating.getScore()).append("/5 ★\n");
        sb.append("Por: ").append(reviewerName).append(" [").append(createdAt.toLocalDate()).append("]\n");
        sb.append("Estado: ").append(status.getDescription()).append("\n");
        sb.append("-----------------------------------------\n");
        sb.append(comment).append("\n");
        sb.append("=========================================");
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("Reseña de '%s' [%d★]: %s", reviewerName, rating.getScore(), reviewTitle);
    }
}
