package com.sena.hidden_pass.application.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class CustomExceptionHandlerResolver{

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleConstraintViolationException(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(BAD_REQUEST).body(ex.getFieldErrors().getFirst().getField() + "= " + ex.getFieldErrors().getFirst().getDefaultMessage());
    }
}
