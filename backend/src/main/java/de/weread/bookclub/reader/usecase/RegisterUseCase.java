package de.weread.bookclub.reader.usecase;

import de.weread.bookclub.reader.domain.Reader;
import de.weread.bookclub.reader.repository.ReaderRepository;
import org.springframework.stereotype.Service;

@Service
public class RegisterUseCase {

    private final ReaderRepository readerRepository;

    public RegisterUseCase(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    public RegisteredEvent execute(RegisterCommand command) {
        final var reader = new Reader();
        reader.setName(command.name);

        final Reader saved = readerRepository.save(reader);

        var registeredEvent = new RegisteredEvent();
        registeredEvent.readerId = saved.getId();
        return registeredEvent;
    }
}
