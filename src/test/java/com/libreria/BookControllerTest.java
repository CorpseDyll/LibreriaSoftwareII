package com.libreria;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/books should return list of books")
    void getAllBooks_shouldReturnBookList() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(15)));
    }

    @Test
    @DisplayName("GET /api/books/search?query=Clean should return matching books")
    void simpleSearchEndpoint_shouldReturnMatchingBooks() throws Exception {
        mockMvc.perform(get("/api/books/search").param("query", "Clean"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isbn").value("978-0132350884"));
    }

    @Test
    @DisplayName("POST /api/books/search/advanced should return matching books by criteria")
    void advancedSearchEndpoint_shouldReturnMatchingBooks() throws Exception {
        String jsonPayload = """
                {
                    "title": "Effective",
                    "author": "Bloch"
                }
                """;

        mockMvc.perform(post("/api/books/search/advanced")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isbn").value("978-0134685991"));
    }

    @Test
    @DisplayName("POST /api/books/{isbn}/ratings should post a rating successfully")
    void rateBookEndpoint_shouldRegisterRating() throws Exception {
        String jsonPayload = """
                {
                    "userId": "estudiante_software",
                    "score": 5
                }
                """;

        mockMvc.perform(post("/api/books/978-0132350884/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Calificación registrada con éxito"));
    }

    @Test
    @DisplayName("POST /api/books/{isbn}/reviews/preview should return review preview")
    void previewReviewEndpoint_shouldReturnPreview() throws Exception {
        String jsonPayload = """
                {
                    "reviewerName": "Carlos",
                    "reviewTitle": "Gran libro",
                    "comment": "Recomendado 100%",
                    "score": 5
                }
                """;

        mockMvc.perform(post("/api/books/978-0132350884/reviews/preview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.preview").exists());
    }

    @Test
    @DisplayName("POST /api/books/{isbn}/reviews should publish review")
    void submitReviewEndpoint_shouldPublishReview() throws Exception {
        String jsonPayload = """
                {
                    "reviewerName": "Carlos",
                    "reviewTitle": "Gran libro",
                    "comment": "Recomendado 100%",
                    "score": 5
                }
                """;

        mockMvc.perform(post("/api/books/978-0132350884/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensaje").value("Reseña publicada con éxito en el libro"));
    }
}
