package de.weread.bookclub.webservice;

import de.weread.bookclub.shelving.usecase.AddBookToShelfCommand;
import de.weread.bookclub.shelving.usecase.AddBookToShelfUseCase;
import de.weread.bookclub.shelving.usecase.FindAllBooksInShelfCommand;
import de.weread.bookclub.shelving.usecase.FindAllBooksInShelfUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("shelfs")
@CrossOrigin(origins = "http://localhost:3000")
public class ShelfingController {

    private final AddBookToShelfUseCase addBookToShelfUseCase;
    private final FindAllBooksInShelfUseCase findAllBooksInShelfUseCase;

    public ShelfingController(AddBookToShelfUseCase addBookToShelfUseCase, FindAllBooksInShelfUseCase findAllBooksInShelfUseCase) {
        this.addBookToShelfUseCase = addBookToShelfUseCase;
        this.findAllBooksInShelfUseCase = findAllBooksInShelfUseCase;
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody AddBookToShelfCommand command) {
        addBookToShelfUseCase.execute(command);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam long reader_id) {
        var command = new FindAllBooksInShelfCommand();
        command.readerId = reader_id;

        return ResponseEntity.ok(findAllBooksInShelfUseCase.execute(command));
    }
}
