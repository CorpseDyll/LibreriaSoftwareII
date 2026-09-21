package com.libreria.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class RatingEmbeddable {

    @Column(name = "score", nullable = false)
    private int score;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public RatingEmbeddable() {
        this.createdAt = LocalDateTime.now();
    }

    public RatingEmbeddable(int score, String userId) {
        validateScore(score);
        this.score = score;
        this.userId = (userId != null && !userId.isBlank()) ? userId.trim() : "Anónimo";
        this.createdAt = LocalDateTime.now();
    }

    public RatingEmbeddable(int score, String userId, LocalDateTime createdAt) {
        validateScore(score);
        this.score = score;
        this.userId = (userId != null && !userId.isBlank()) ? userId.trim() : "Anónimo";
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }

    private void validateScore(int score) {
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 (malo) y 5 (bueno). Valor proporcionado: " + score);
        }
    }

    public int getScore() {
        return score;
    }

    public String getUserId() {
        return userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RatingEmbeddable rating = (RatingEmbeddable) o;
        return score == rating.score && Objects.equals(userId, rating.userId) && Objects.equals(createdAt, rating.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(score, userId, createdAt);
    }

    @Override
    public String toString() {
        return String.format("%d★ por %s", score, userId);
    }
}
