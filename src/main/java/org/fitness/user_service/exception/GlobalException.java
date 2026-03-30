package org.fitness.user_service.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MissingParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameterException(MissingParameterException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getStatus());
        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(InvalidUuidException.class)
    public ResponseEntity<ErrorResponse> handleInvalidUuidException(InvalidUuidException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getStatus());
        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getStatus());
        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }
    @ExceptionHandler(DuplicateValueException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateValueException(DuplicateValueException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getStatus());
        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Invalid username or password", HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UsernameNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Username not found",
                        HttpStatus.NOT_FOUND)); // same message — don't leak which field is wrong
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getStatus());
        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        if (UUID.class.equals(ex.getRequiredType())) {
            return handleInvalidUuidException(new InvalidUuidException("Invalid UUID in request parameter"));
        }
        ErrorResponse errorResponse = new ErrorResponse("Invalid request parameter", HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getConstraintViolations()
                .forEach(v -> errors.put(v.getPropertyPath().toString(), v.getMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        if (isInvalidUuidInRequestBody(ex)) {
            return handleInvalidUuidException(new InvalidUuidException("Invalid UUID in request body"));
        }
        ErrorResponse errorResponse = new ErrorResponse("Malformed JSON request", HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    private boolean isInvalidUuidInRequestBody(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException invalidFormatException) {
            return UUID.class.equals(invalidFormatException.getTargetType());
        }
        return false;
    }

    @ExceptionHandler(FeignClientException.class)
    public ResponseEntity<ErrorResponse> handleFeignClientException(FeignClientException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getHttpStatus());
        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
    }
}
