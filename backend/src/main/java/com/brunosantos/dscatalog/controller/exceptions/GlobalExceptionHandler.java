package com.brunosantos.dscatalog.controller.exceptions;

import com.brunosantos.dscatalog.services.exceptions.DatabaseException;
import com.brunosantos.dscatalog.services.exceptions.InvalidLoginException;
import com.brunosantos.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.security.auth.login.LoginException;
import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final StandardError err;
    private final ErrorResponse errorResponse;

    public GlobalExceptionHandler(StandardError err, ErrorResponse errorResponse) {
        this.err = err;
        this.errorResponse = errorResponse;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("Resource not found");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> database(DatabaseException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("Database exception");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_CONTENT;
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("Database exception");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());

        for(FieldError f : e.getBindingResult().getFieldErrors()) {
            errorResponse.addError(new Error(f.getField(), f.getDefaultMessage()));
        }
        return ResponseEntity.status(status).body(errorResponse);
    }

}
