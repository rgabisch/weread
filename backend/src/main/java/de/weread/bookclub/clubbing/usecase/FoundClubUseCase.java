package de.weread.bookclub.clubbing.usecase;

import de.weread.bookclub.clubbing.domain.Club;
import de.weread.bookclub.reader.domain.Reader;
import de.weread.bookclub.reader.repository.ReaderRepository;
import org.springframework.stereotype.Service;

@Service
public class FoundClubUseCase {
    private final ReaderRepository readerRepository;

    public FoundClubUseCase(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    public void execute(FoundClubCommand command) {
        readerRepository
                .findById(command.readerId)
                .map(reader -> addClub(reader, new Club()))
                .ifPresent(readerRepository::save);
    }

    private Reader addClub(Reader reader, Club club) {
        reader.getClubs().add(club);
        return reader;
    }
}
