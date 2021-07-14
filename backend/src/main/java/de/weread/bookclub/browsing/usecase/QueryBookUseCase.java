package de.weread.bookclub.browsing.usecase;

import de.weread.bookclub.browsing.domain.Book;
import de.weread.bookclub.browsing.repository.LibraryRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class QueryBookUseCase {

    private final LibraryRepository libraryRepository;

    public QueryBookUseCase(LibraryRepository libraryRepository) {
        this.libraryRepository = libraryRepository;
    }

    public Set<Book> execute(QueryBookCommand command) {
        return libraryRepository.findBy(command.query);
    }
}

