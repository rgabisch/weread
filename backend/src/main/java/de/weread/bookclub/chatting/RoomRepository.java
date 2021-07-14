package de.weread.bookclub.chatting;

import de.weread.bookclub.clubbing.domain.Club;
import de.weread.bookclub.reader.domain.Reader;

public interface RoomRepository {
    String openWith(String name);

    Club join(Club club, Reader reader);
}
