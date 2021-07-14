package de.weread.bookclub.webservice;

import de.weread.bookclub.browsing.domain.Book;
import de.weread.bookclub.browsing.usecase.QueryBookCommand;
import de.weread.bookclub.browsing.usecase.QueryBookUseCase;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value = "books", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:3000")
public class BrowsingController {

    private final QueryBookUseCase queryBookUseCase;

    public BrowsingController(QueryBookUseCase queryBookUseCase) {
        this.queryBookUseCase = queryBookUseCase;
    }

    @GetMapping
    public ResponseEntity<?> query(@RequestParam String query) {
        var queryCommand = new QueryBookCommand();
        queryCommand.query = query;

        final Set<Book> execute = queryBookUseCase.execute(queryCommand);
        return ResponseEntity.ok().body(execute);
    }
}
