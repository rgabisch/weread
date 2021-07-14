package de.weread.bookclub.shelving.usecase;

import de.weread.bookclub.browsing.domain.Book;
import de.weread.bookclub.clubbing.domain.Club;
import de.weread.bookclub.clubbing.repository.ClubRepository;
import de.weread.bookclub.reader.domain.Reader;
import de.weread.bookclub.reader.repository.ReaderRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;

@Service
public class FindAllBooksInShelfUseCase {

    private final ReaderRepository readerRepository;
    private final ClubRepository clubRepository;

    public FindAllBooksInShelfUseCase(ReaderRepository readerRepository, ClubRepository clubRepository) {
        this.readerRepository = readerRepository;
        this.clubRepository = clubRepository;
    }

    public Set<FoundBooksInShelfEvent> execute(FindAllBooksInShelfCommand command) {
        return readerRepository
                .findById(command.readerId)
                .map(this::mapToBookRepresentation)
                .orElse(emptySet());
    }

    Set<FoundBooksInShelfEvent> mapToBookRepresentation(Reader reader) {
        return reader.getShelf()
                     .getBooks()
                     .stream()
                     .map(book -> new FoundBooksInShelfEvent(book.getId(),
                                                             book.getTitle(),
                                                             book.getAuthor(),
                                                             book.getCover(),
                                                             clubRepository.countByBook(book),
                                                             doesReaderContainsBook(reader, book),
                                                             clubIdOf(book, reader)))
                     .collect(Collectors.toSet());
    }

    private boolean doesReaderContainsBook(Reader reader, Book book) {
        return reader.getClubs()
                     .stream()
                     .map(Club::getBook)
                     .anyMatch(clubBook -> clubBook.equals(book));
    }


    private long clubIdOf(Book book, Reader reader) {
        return reader.getClubs()
                     .stream()
                     .filter(club -> club.getBook().equals(book))
                     .findFirst()
                     .map(Club::getId)
                     .orElse(-1L);
    }
}