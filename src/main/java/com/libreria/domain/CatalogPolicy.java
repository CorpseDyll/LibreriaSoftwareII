package com.libreria.domain;

/**
 * Regla de negocio y política de catálogo:
 * Inicialmente solo se permite vender libros y se restringen productos de alto valor (>$10,000).
 * La clase permite ser reconfigurada en el futuro cuando el sitio expanda sus ventas a todo el catálogo.
 */
public class CatalogPolicy {
    private boolean onlyBooksAllowed;
    private double maxAllowedPrice;

    public CatalogPolicy() {
        // Configuración inicial de la empresa (Planned Change)
        this.onlyBooksAllowed = true;
        this.maxAllowedPrice = 10000.00;
    }

    public CatalogPolicy(boolean onlyBooksAllowed, double maxAllowedPrice) {
        this.onlyBooksAllowed = onlyBooksAllowed;
        this.maxAllowedPrice = maxAllowedPrice;
    }

    public boolean isProductAllowedForSale(Product product) {
        if (product == null) {
            return false;
        }

        // Si la política está restringida solo a libros, bloquear cualquier otro tipo de producto
        if (onlyBooksAllowed && product.getProductType() != ProductType.BOOK) {
            return false;
        }

        // Restricción de ítems de alto valor (> $10,000)
        if (product.getPrice() > maxAllowedPrice) {
            return false;
        }

        return true;
    }

    public String getRejectionReason(Product product) {
        if (product == null) {
            return "El producto es nulo.";
        }
        if (onlyBooksAllowed && product.getProductType() != ProductType.BOOK) {
            return String.format("Actualmente solo se venden libros. El producto '%s' es de tipo '%s'.",
                    product.getName(), product.getProductType().getDisplayName());
        }
        if (product.getPrice() > maxAllowedPrice) {
            return String.format("El precio ($%.2f) supera el límite máximo permitido para venta online ($%.2f).",
                    product.getPrice(), maxAllowedPrice);
        }
        return "El producto está habilitado para la venta.";
    }

    public boolean isOnlyBooksAllowed() {
        return onlyBooksAllowed;
    }

    public void setOnlyBooksAllowed(boolean onlyBooksAllowed) {
        this.onlyBooksAllowed = onlyBooksAllowed;
    }

    public double getMaxAllowedPrice() {
        return maxAllowedPrice;
    }

    public void setMaxAllowedPrice(double maxAllowedPrice) {
        this.maxAllowedPrice = maxAllowedPrice;
    }
}
