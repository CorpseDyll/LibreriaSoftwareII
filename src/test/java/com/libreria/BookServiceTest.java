package com.libreria;

import com.libreria.domain.Book;
import com.libreria.domain.Review;
import com.libreria.domain.ReviewStatus;
import com.libreria.search.SearchCriteria;
import com.libreria.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BookServiceTest {

    private BookService service;

    @BeforeEach
    void setUp() {
        service = new BookService();
    }

    @Test
    @DisplayName("Requirement 1: Basic simple search in both author and title fields")
    void userStory1_simpleSearch() {
        List<Book> results = service.simpleSearch("Robert");
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("Clean Code: A Handbook of Agile Software Craftsmanship");

        List<Book> results2 = service.simpleSearch("Design");
        assertThat(results2).hasSize(2);
    }

    @Test
    @DisplayName("Requirement 2: Advanced search by combination of author, title, ISBN")
    void userStory2_advancedSearch() {
        SearchCriteria criteria = SearchCriteria.builder()
                .title("Effective")
                .author("Bloch")
                .isbn("978-0134685991")
                .build();

        List<Book> results = service.advancedSearch(criteria);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getAuthor()).isEqualTo("Joshua Bloch");
    }

    @Test
    @DisplayName("Requirement 3: Rate books from 1 (bad) to 5 (good) without purchase check")
    void userStory3_rateBook() {
        String isbn = "978-0132350884"; // Clean Code

        service.rateBook(isbn, "user123", 5);
        service.rateBook(isbn, "user456", 4);

        Book book = service.getRepository().findByIsbn(isbn).orElseThrow();
        assertThat(book.getTotalRatingsCount()).isEqualTo(2);
        assertThat(book.getAverageRating()).isEqualTo(4.5);

        // Invalid rating < 1 or > 5
        assertThatThrownBy(() -> service.rateBook(isbn, "user789", 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La calificación debe estar entre 1 (malo) y 5 (bueno)");

        assertThatThrownBy(() -> service.rateBook(isbn, "user789", 6))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La calificación debe estar entre 1 (malo) y 5 (bueno)");
    }

    @Test
    @DisplayName("Requirement 4: Write review, preview before submitting, no purchase required")
    void userStory4_writePreviewAndSubmitReview() {
        String isbn = "978-0201633610"; // Design Patterns

        // Step 1: Create draft
        Review draft = service.createReviewDraft("Ana Maria", "Excelente referente de software",
                "Un libro imprescindible para cualquier desarrollador de software orientada a objetos.", 5);

        assertThat(draft.getStatus()).isEqualTo(ReviewStatus.DRAFT);

        // Step 2: Preview review before submitting
        String previewText = service.previewReview(isbn, draft);
        assertThat(previewText).contains("VISTA PREVIA DE RESEÑA");
        assertThat(previewText).contains("Excelente referente de software");
        assertThat(previewText).contains("Ana Maria");

        // Step 3: Submit review
        service.submitReview(isbn, draft);

        Book book = service.getRepository().findByIsbn(isbn).orElseThrow();
        assertThat(book.getReviews()).hasSize(1);
        assertThat(book.getReviews().get(0).getStatus()).isEqualTo(ReviewStatus.SUBMITTED);
        assertThat(book.getAverageRating()).isEqualTo(5.0);
    }
}
