package de.weread.bookclub.clubbing.usecase;

import de.weread.bookclub.chatting.RoomRepository;
import de.weread.bookclub.clubbing.domain.Club;
import de.weread.bookclub.clubbing.repository.ClubRepository;
import de.weread.bookclub.reader.domain.Reader;
import de.weread.bookclub.reader.repository.ReaderRepository;
import de.weread.bookclub.shelving.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class JoinClubUseCase {

    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final ClubRepository clubRepository;
    private final RoomRepository roomRepository;

    public JoinClubUseCase(BookRepository bookRepository, ReaderRepository readerRepository, ClubRepository clubRepository, RoomRepository roomRepository) {
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.clubRepository = clubRepository;
        this.roomRepository = roomRepository;
    }

    public void execute(JoinClubCommand command) throws IOException {
        final Optional<Club> clubByID = clubRepository.findById(command.clubId);

        if (clubByID.isPresent()) {
            Club club = clubByID.get();
            club.setAmountOfReaders(club.getAmountOfReaders() + 1);
            readerRepository.findById(command.readerId)
                            .map(reader -> addClub(reader, club))
                            .ifPresent(readerRepository::save);
        } else {
            Club club = new Club();
            club.setAmountOfReaders(1);
            var roomName = UUID.randomUUID().toString();
            var roomURL = roomRepository.openWith(roomName);
            club.setUrl(roomURL);
            club.setName(roomName);
            var book = bookRepository.findById(command.isbn).get();
            club.setBook(book);

            clubRepository.save(club);

            readerRepository.findById(command.readerId)
                            .map(reader -> addClub(reader, club))
                            .ifPresent(readerRepository::save);
        }
    }

    private Reader addClub(Reader reader, Club club) {
        reader.getClubs().add(club);
        return reader;
    }

}
