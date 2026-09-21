package com.libreria.repository;

import com.libreria.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, String>, JpaSpecificationExecutor<Book> {

    Optional<Book> findByIsbn(String isbn);

    /**
     * Búsqueda simple: Busca coincidencia de texto (case-insensitive) tanto en autor como en título.
     */
    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String titleQuery, String authorQuery);
}
