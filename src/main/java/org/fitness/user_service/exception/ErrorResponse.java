package org.fitness.user_service.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.LocalDateTime;


@Getter
@Setter
public class ErrorResponse {
    private String message;
    private HttpStatus status;
    private Instant timestamp;

    public ErrorResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
        this.timestamp = Instant.now();
    }


}
