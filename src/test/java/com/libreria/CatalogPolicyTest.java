package com.libreria;

import com.libreria.domain.Book;
import com.libreria.domain.CatalogPolicy;
import com.libreria.domain.Product;
import com.libreria.domain.ProductType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CatalogPolicyTest {

    private CatalogPolicy policy;

    @BeforeEach
    void setUp() {
        policy = new CatalogPolicy(); // Default: books only, max price $10,000
    }

    @Test
    @DisplayName("Policy should allow books under $10,000")
    void policy_shouldAllowNormalBook() {
        Book normalBook = new Book("1", "978-1111111111", "Java 101", "Author", 49.99, "Tech", "Intro to Java");
        assertThat(policy.isProductAllowedForSale(normalBook)).isTrue();
    }

    @Test
    @DisplayName("Policy should reject high-ticket items costing over $10,000")
    void policy_shouldRejectExpensiveItems() {
        Book expensiveCollectorBook = new Book("2", "978-9999999999", "Rare Ancient Codex", "Ancient Monk", 15000.00, "History", "Rare item");
        assertThat(policy.isProductAllowedForSale(expensiveCollectorBook)).isFalse();
        assertThat(policy.getRejectionReason(expensiveCollectorBook)).contains("supera el límite máximo permitido");
    }

    @Test
    @DisplayName("Policy should reject non-book products during initial planned change phase")
    void policy_shouldRejectNonBooksInitially() {
        Product laptop = new Product("E001", "Gaming Laptop", 1500.00, "High perf laptop", ProductType.ELECTRONICS) {};
        assertThat(policy.isProductAllowedForSale(laptop)).isFalse();
        assertThat(policy.getRejectionReason(laptop)).contains("Actualmente solo se venden libros");
    }

    @Test
    @DisplayName("Policy should allow non-book products when expansion policy is activated")
    void policy_shouldAllowNonBooksWhenExpanded() {
        policy.setOnlyBooksAllowed(false);

        Product laptop = new Product("E001", "Gaming Laptop", 1500.00, "High perf laptop", ProductType.ELECTRONICS) {};
        assertThat(policy.isProductAllowedForSale(laptop)).isTrue();

        Product expensiveJewelry = new Product("J001", "Diamond Necklace", 12000.00, "Luxury diamond", ProductType.JEWELRY) {};
        assertThat(policy.isProductAllowedForSale(expensiveJewelry)).isFalse(); // Still blocked by $10,000 limit
    }
}
