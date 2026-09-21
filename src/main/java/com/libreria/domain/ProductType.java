package com.libreria.domain;

public enum ProductType {
    BOOK("Libro"),
    ELECTRONICS("Electrónica"),
    JEWELRY("Joyería"),
    OTHER("Otros");

    private final String displayName;

    ProductType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
