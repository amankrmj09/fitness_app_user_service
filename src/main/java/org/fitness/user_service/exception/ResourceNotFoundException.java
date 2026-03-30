package org.fitness.user_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RuntimeException {
    @Getter
    private final String message;

    @Getter
    private final HttpStatus status;

    public ResourceNotFoundException(String message) {
        super(message);
        this.message = message;
        this.status = HttpStatus.NOT_FOUND;
    }
}
