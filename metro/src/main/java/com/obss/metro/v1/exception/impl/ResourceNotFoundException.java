package com.obss.metro.v1.exception.impl;

import com.obss.metro.v1.exception.MetroError;
import com.obss.metro.v1.exception.MetroException;
import java.util.Set;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends MetroException {
  public ResourceNotFoundException(final String field, final String message) {
    super(
        HttpStatus.NOT_FOUND.value(),
        HttpStatus.NOT_FOUND.getReasonPhrase(),
        Set.of(new MetroError(field, message)));
  }
}
