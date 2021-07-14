package de.weread.bookclub.webservice;

import de.weread.bookclub.clubbing.usecase.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "clubs", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:3000")
public class ClubController {

    private final JoinClubUseCase joinClubUseCase;
    private final FoundClubUseCase foundClubUseCase;
    private final QueryClubsOfReaderUseCase queryClubsOfReaderUseCase;
    private final QueryClubsOfBookUseCase queryClubsOfBookUseCase;

    public ClubController(JoinClubUseCase joinClubUseCase, FoundClubUseCase foundClubUseCase, QueryClubsOfReaderUseCase queryClubsOfReaderUseCase, QueryClubsOfBookUseCase queryClubsOfBookUseCase) {
        this.joinClubUseCase = joinClubUseCase;
        this.foundClubUseCase = foundClubUseCase;
        this.queryClubsOfReaderUseCase = queryClubsOfReaderUseCase;
        this.queryClubsOfBookUseCase = queryClubsOfBookUseCase;
    }

    @PostMapping("/{clubId}")
    public ResponseEntity<?> join(@PathVariable String clubId, @RequestBody JoinClubCommand joinClubCommand) throws IOException {
        joinClubCommand.clubId = Long.parseLong(clubId);
        joinClubUseCase.execute(joinClubCommand);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> found(@RequestBody FoundClubCommand command) {
        foundClubUseCase.execute(command);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{readerId}")
    public ResponseEntity<?> query(@PathVariable String readerId) {
        var queryCommand = new QueryClubsOfReaderCommand();
        queryCommand.readerId = Long.parseLong(readerId);

        return ResponseEntity.ok(queryClubsOfReaderUseCase.execute(queryCommand));
    }

    @GetMapping
    public ResponseEntity<?> query(@RequestParam String reader_id, @RequestParam String isbn) {
        var queryCommand = new QueryClubsOfBookCommand();
        queryCommand.readerId = Long.parseLong(reader_id);
        queryCommand.isbn = isbn;

        return ResponseEntity.ok(queryClubsOfBookUseCase.execute(queryCommand));
    }
}
