package org.fitness.user_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidUuidException extends RuntimeException {

    private final String message;
    private final HttpStatus status;

    public InvalidUuidException(String message) {
        super(message);
        this.message = message;
        this.status = HttpStatus.BAD_REQUEST;
    }
}
