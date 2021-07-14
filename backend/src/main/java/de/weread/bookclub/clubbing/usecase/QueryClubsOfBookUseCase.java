package de.weread.bookclub.clubbing.usecase;

import de.weread.bookclub.browsing.domain.Book;
import de.weread.bookclub.chatting.RoomRepository;
import de.weread.bookclub.clubbing.domain.Club;
import de.weread.bookclub.clubbing.repository.ClubRepository;
import de.weread.bookclub.reader.domain.Reader;
import de.weread.bookclub.reader.repository.ReaderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;

@Service
public class QueryClubsOfBookUseCase {
    private final ReaderRepository readerRepository;
    private final ClubRepository clubRepository;
    private final RoomRepository roomRepository;

    public QueryClubsOfBookUseCase(ReaderRepository readerRepository, ClubRepository clubRepository, RoomRepository roomRepository) {
        this.readerRepository = readerRepository;
        this.clubRepository = clubRepository;
        this.roomRepository = roomRepository;
    }

    public Set<Club> execute(QueryClubsOfBookCommand command) {
        return readerRepository.findById(command.readerId)
                               .flatMap(reader -> findBook(reader, new Book(command.isbn)))
                               .flatMap(clubRepository::findByBook)
                               .orElse(emptySet())
                               .stream()
                               .map(club -> roomRepository
                                       .join(club, readerRepository.findById(command.readerId).get()))
                               .collect(Collectors.toSet());
    }

    private Optional<Book> findBook(Reader reader, Book book) {
        return reader.getShelf()
                     .getBooks()
                     .stream()
                     .filter(book::equals)
                     .findFirst();
    }

}
