package de.weread.bookclub.shelving.repository;

import de.weread.bookclub.browsing.domain.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<Book, String> {
}