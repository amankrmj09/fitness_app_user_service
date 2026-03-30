package org.fitness.user_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MissingParameterException extends RuntimeException {

    private final String message;
    private final HttpStatus status;

    public MissingParameterException(String message, String message1, HttpStatus status) {
        super(message);
        this.message = message1;
        this.status = status;
    }
}
