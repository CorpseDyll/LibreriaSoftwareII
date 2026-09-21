package com.libreria.domain;

import java.util.Objects;

public abstract class Product {
    private final String id;
    private final String name;
    private final double price;
    private final String description;
    private final ProductType productType;

    public Product(String id, String name, double price, String description, ProductType productType) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID del producto no puede estar vacío.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description != null ? description : "";
        this.productType = Objects.requireNonNull(productType, "El tipo de producto es obligatorio.");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public ProductType getProductType() {
        return productType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s [ID=%s, Nombre='%s', Precio=$%.2f]",
                productType.getDisplayName(), id, name, price);
    }
}
