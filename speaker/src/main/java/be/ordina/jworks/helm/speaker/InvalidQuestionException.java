package be.ordina.jworks.helm.speaker;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidQuestionException extends ResponseStatusException {


    public InvalidQuestionException() {
        super(HttpStatus.BAD_REQUEST, "Invalid question");
    }
}
