package de.weread.bookclub.clubbing.repository;

import de.weread.bookclub.browsing.domain.Book;
import de.weread.bookclub.clubbing.domain.Club;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ClubRepository extends CrudRepository<Club, Long> {
    int countByBook(Book book);
    Optional<Set<Club>> findByBook(Book book);
}
