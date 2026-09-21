package com.libreria.search;

import com.libreria.domain.Book;
import java.util.List;

public interface SearchStrategy {
    List<Book> search(List<Book> books);
}
