package com.example.booking.exception;

import com.example.booking.payload.response.BadRequestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BadRequestResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        BadRequestResponse errors = new BadRequestResponse();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.addErrors(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BadRequestResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        BadRequestResponse errors = new BadRequestResponse();
        errors.addErrors("error", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<BadRequestResponse> handleNullPointerException(NullPointerException ex, WebRequest request) {
        BadRequestResponse errors = new BadRequestResponse();
        errors.addErrors("error", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException .class)
    public ResponseEntity<Object> handleNullPointerException(RuntimeException  ex, WebRequest request) {
        BadRequestResponse errors = new BadRequestResponse();
        errors.addErrors("error", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}