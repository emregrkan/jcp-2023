package com.obss.metro.v1.exception;

import java.util.Set;

// todo: should make an exception superclass
public record ValidationException(String message, Set<Violation> violations) {
  public ValidationException(final Set<Violation> violations) {
    this("Validation Failed", violations);
  }

  public record Violation(String field, String message) {}
}
