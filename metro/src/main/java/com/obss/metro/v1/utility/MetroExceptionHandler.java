package com.obss.metro.v1.utility;

import com.obss.metro.v1.exception.ValidationException;
import jakarta.validation.ConstraintViolationException;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// todo: not sure if this is the right way
// todo: document this
// todo: handle json parse

@RestControllerAdvice
public class MetroExceptionHandler {
  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public ResponseEntity<ValidationException> handleConstraintViolations(
      final ConstraintViolationException exception) {
    final Set<ValidationException.Violation> errors =
        exception.getConstraintViolations().parallelStream()
            .map(
                violation ->
                    new ValidationException.Violation(
                        violation.getPropertyPath().toString(), violation.getMessage()))
            .collect(Collectors.toSet());

    return ResponseEntity.status(422).body(new ValidationException(errors));
  }
}
