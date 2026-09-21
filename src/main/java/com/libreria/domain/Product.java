package com.libreria.domain;

import jakarta.persistence.*;
import java.util.Objects;

@MappedSuperclass
public abstract class Product {

    @Id
    @Column(name = "id", nullable = false, length = 50)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "description", length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", nullable = false)
    private ProductType productType;

    public Product() {
    }

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
        this.id = id.trim();
        this.name = name.trim();
        this.price = price;
        this.description = description != null ? description.trim() : "";
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
