package com.libreria.search;

import com.libreria.domain.Book;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Requisito: "A user can search for books by entering values in any combination of author, title and ISBN"
 */
public class AdvancedSearchStrategy implements SearchStrategy {
    private final SearchCriteria criteria;

    public AdvancedSearchStrategy(SearchCriteria criteria) {
        this.criteria = criteria != null ? criteria : new SearchCriteria("", "", "");
    }

    @Override
    public List<Book> search(List<Book> books) {
        if (books == null || books.isEmpty()) {
            return Collections.emptyList();
        }

        if (criteria.isEmpty()) {
            return books;
        }

        return books.stream()
                .filter(this::matchesCriteria)
                .collect(Collectors.toList());
    }

    private boolean matchesCriteria(Book book) {
        // All non-empty criteria fields must match (AND condition across provided filters)
        if (!criteria.getAuthor().isEmpty()) {
            if (!book.getAuthor().toLowerCase().contains(criteria.getAuthor().toLowerCase())) {
                return false;
            }
        }

        if (!criteria.getTitle().isEmpty()) {
            if (!book.getTitle().toLowerCase().contains(criteria.getTitle().toLowerCase())) {
                return false;
            }
        }

        if (!criteria.getIsbn().isEmpty()) {
            String cleanIsbnQuery = criteria.getIsbn().replace("-", "").toLowerCase();
            String cleanBookIsbn = book.getIsbn().replace("-", "").toLowerCase();
            if (!cleanBookIsbn.contains(cleanIsbnQuery)) {
                return false;
            }
        }

        return true;
    }
}
