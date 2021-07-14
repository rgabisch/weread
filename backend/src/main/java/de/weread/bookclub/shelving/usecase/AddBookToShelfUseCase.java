package de.weread.bookclub.shelving.usecase;

import de.weread.bookclub.browsing.domain.Book;
import de.weread.bookclub.reader.domain.Reader;
import de.weread.bookclub.reader.repository.ReaderRepository;
import de.weread.bookclub.shelving.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class AddBookToShelfUseCase {

    private final ReaderRepository readerRepository;
    private final BookRepository bookRepository;

    public AddBookToShelfUseCase(ReaderRepository readerRepository, BookRepository bookRepository) {
        this.readerRepository = readerRepository;
        this.bookRepository = bookRepository;
    }

    public void execute(AddBookToShelfCommand command) {
        bookRepository.save(new Book(command.isbn, command.title, command.author, command.cover));

        readerRepository
                .findById(command.readerId)
                .map(reader -> addBook(reader, new Book(command.isbn)))
                .map(readerRepository::save);
    }

    private Reader addBook(Reader reader, Book book) {
        var shelf = reader.getShelf();
        shelf.addBook(book);
        reader.setShelf(shelf);

        return reader;
    }
}
