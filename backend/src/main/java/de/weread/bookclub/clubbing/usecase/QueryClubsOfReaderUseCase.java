package de.weread.bookclub.clubbing.usecase;

import de.weread.bookclub.chatting.RoomRepository;
import de.weread.bookclub.clubbing.domain.Club;
import de.weread.bookclub.reader.domain.Reader;
import de.weread.bookclub.reader.repository.ReaderRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class QueryClubsOfReaderUseCase {
    private final ReaderRepository readerRepository;
    private final RoomRepository roomRepository;

    public QueryClubsOfReaderUseCase(ReaderRepository readerRepository, RoomRepository roomRepository) {
        this.readerRepository = readerRepository;
        this.roomRepository = roomRepository;
    }

    public Set<Club> execute(QueryClubsOfReaderCommand queryClubsOfReaderCommand) {
        return readerRepository.findById(queryClubsOfReaderCommand.readerId)
                               .map(this::buildClubs)
                               .orElse(Collections.emptySet());

    }

    private Set<Club> buildClubs(Reader reader) {
        return reader.getClubs()
                     .stream()
                     .peek(club -> roomRepository.join(club, reader))
                     .collect(Collectors.toSet());
    }
}
