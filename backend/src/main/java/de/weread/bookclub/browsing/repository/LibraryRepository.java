package de.weread.bookclub.browsing.repository;

import de.weread.bookclub.browsing.domain.Book;

import java.util.Set;

public interface LibraryRepository {
    Set<Book> findBy(String query);
}
