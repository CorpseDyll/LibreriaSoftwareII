package com.libreria.domain;

public enum ReviewStatus {
    DRAFT("Borrador (Vista Previa)"),
    SUBMITTED("Publicada");

    private final String description;

    ReviewStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
