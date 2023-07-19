package com.obss.metro.utility;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// todo: not sure if this is the right way
// todo: document this

@RestControllerAdvice
public class MetroExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Set<String>>> handleConstraintViolations(final ConstraintViolationException exception) {
        final Set<String> violations = exception.getConstraintViolations().parallelStream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());
        final Map<String, Set<String>> response = new HashMap<>(1) {{
            put("errors", violations);
        }};

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
