package com.obss.metro.v1.exception.impl;

import com.obss.metro.v1.exception.MetroError;
import com.obss.metro.v1.exception.MetroException;
import java.util.Set;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends MetroException {

  public ForbiddenException() {
    super(
        HttpStatus.FORBIDDEN.value(),
        HttpStatus.FORBIDDEN.getReasonPhrase(),
        Set.of(
            new MetroError("Authorization", "You do not have permission to access this resource")));
  }
}
