package com.libreria.search;

import com.libreria.domain.Book;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Requisito: "A user can do a basic simple search that searches for a word or phrase in both the author and title fields."
 */
public class SimpleSearchStrategy implements SearchStrategy {
    private final String query;

    public SimpleSearchStrategy(String query) {
        this.query = query != null ? query.trim().toLowerCase() : "";
    }

    @Override
    public List<Book> search(List<Book> books) {
        if (books == null || books.isEmpty() || query.isEmpty()) {
            return books != null ? books : Collections.emptyList();
        }

        return books.stream()
                .filter(book -> matchesSimpleQuery(book, query))
                .collect(Collectors.toList());
    }

    private boolean matchesSimpleQuery(Book book, String lowerQuery) {
        String title = book.getTitle().toLowerCase();
        String author = book.getAuthor().toLowerCase();

        return title.contains(lowerQuery) || author.contains(lowerQuery);
    }
}
