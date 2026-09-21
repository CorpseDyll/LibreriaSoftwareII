package com.libreria.search;

public class SearchCriteria {
    private final String author;
    private final String title;
    private final String isbn;

    public SearchCriteria(String author, String title, String isbn) {
        this.author = author != null ? author.trim() : "";
        this.title = title != null ? title.trim() : "";
        this.isbn = isbn != null ? isbn.trim() : "";
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public boolean isEmpty() {
        return author.isEmpty() && title.isEmpty() && isbn.isEmpty();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String author;
        private String title;
        private String isbn;

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder isbn(String isbn) {
            this.isbn = isbn;
            return this;
        }

        public SearchCriteria build() {
            return new SearchCriteria(author, title, isbn);
        }
    }
}
