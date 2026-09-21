package com.libreria;

import com.libreria.domain.Book;
import com.libreria.search.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SearchStrategyTest {

    private List<Book> books;

    @BeforeEach
    void setUp() {
        Book b1 = new Book("1", "978-0132350884", "Clean Code", "Robert C. Martin", 42.50, "Software", "Clean code principles");
        Book b2 = new Book("2", "978-0201633610", "Design Patterns", "Erich Gamma", 54.99, "Software", "GoF Patterns");
        Book b3 = new Book("3", "978-0134685991", "Effective Java", "Joshua Bloch", 49.99, "Software", "Java Best Practices");
        Book b4 = new Book("4", "978-0596007126", "Head First Design Patterns", "Elisabeth Robson", 38.00, "Software", "Visual patterns");

        books = List.of(b1, b2, b3, b4);
    }

    @Test
    @DisplayName("Simple search should match keyword in both title and author fields")
    void simpleSearch_shouldMatchTitleOrAuthor() {
        // Query matching title 'Clean'
        SimpleSearchStrategy search1 = new SimpleSearchStrategy("Clean");
        List<Book> results1 = search1.search(books);
        assertThat(results1).hasSize(1);
        assertThat(results1.get(0).getTitle()).isEqualTo("Clean Code");

        // Query matching author 'Bloch'
        SimpleSearchStrategy search2 = new SimpleSearchStrategy("Bloch");
        List<Book> results2 = search2.search(books);
        assertThat(results2).hasSize(1);
        assertThat(results2.get(0).getAuthor()).isEqualTo("Joshua Bloch");

        // Query matching both 'Design Patterns' (found in title of b2 and b4)
        SimpleSearchStrategy search3 = new SimpleSearchStrategy("Design Patterns");
        List<Book> results3 = search3.search(books);
        assertThat(results3).hasSize(2);
    }

    @Test
    @DisplayName("Advanced search should match any combination of author, title and ISBN")
    void advancedSearch_shouldMatchCombinationCriteria() {
        // Combination: Author contains 'Martin' and Title contains 'Code'
        SearchCriteria criteria1 = SearchCriteria.builder()
                .author("Martin")
                .title("Code")
                .build();
        AdvancedSearchStrategy strategy1 = new AdvancedSearchStrategy(criteria1);
        List<Book> results1 = strategy1.search(books);
        assertThat(results1).hasSize(1);
        assertThat(results1.get(0).getIsbn()).isEqualTo("978-0132350884");

        // Combination: Search by ISBN only
        SearchCriteria criteria2 = SearchCriteria.builder()
                .isbn("978-0134685991")
                .build();
        AdvancedSearchStrategy strategy2 = new AdvancedSearchStrategy(criteria2);
        List<Book> results2 = strategy2.search(books);
        assertThat(results2).hasSize(1);
        assertThat(results2.get(0).getTitle()).isEqualTo("Effective Java");

        // Combination with no match
        SearchCriteria criteria3 = SearchCriteria.builder()
                .author("Joshua Bloch")
                .title("Clean Code")
                .build();
        AdvancedSearchStrategy strategy3 = new AdvancedSearchStrategy(criteria3);
        List<Book> results3 = strategy3.search(books);
        assertThat(results3).isEmpty();
    }
}
