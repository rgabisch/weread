package de.weread.bookclub.webservice;

import de.weread.bookclub.reader.usecase.RegisterCommand;
import de.weread.bookclub.reader.usecase.RegisterUseCase;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "readers", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:3000")
public class ReaderController {

    private final RegisterUseCase registerUseCase;

    public ReaderController(RegisterUseCase registerUseCase) {
        this.registerUseCase = registerUseCase;
    }

    @PostMapping()
    public ResponseEntity<?> join(@RequestBody RegisterCommand command) {
        var event = registerUseCase.execute(command);

        return ResponseEntity.ok(event);
    }
}
