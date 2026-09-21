package com.libreria.domain;

import java.time.LocalDateTime;

public class Rating {
    private final int score; // 1 (bad) to 5 (good)
    private final String userId;
    private final LocalDateTime createdAt;

    public Rating(int score, String userId) {
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 (malo) y 5 (bueno). Valor proporcionado: " + score);
        }
        this.score = score;
        this.userId = (userId != null && !userId.isBlank()) ? userId : "Anónimo";
        this.createdAt = LocalDateTime.now();
    }

    public Rating(int score, String userId, LocalDateTime createdAt) {
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 (malo) y 5 (bueno). Valor proporcionado: " + score);
        }
        this.score = score;
        this.userId = (userId != null && !userId.isBlank()) ? userId : "Anónimo";
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
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
    public String toString() {
        return String.format("%d★ por %s (%s)", score, userId, createdAt.toLocalDate());
    }
}
