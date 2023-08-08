package com.obss.metro.v1.exception.impl;

import com.obss.metro.v1.exception.MetroError;
import com.obss.metro.v1.exception.MetroException;
import java.util.Set;
import org.springframework.http.HttpStatus;

public class UnprocessableEntityException extends MetroException {

  public UnprocessableEntityException(final Set<MetroError> errors) {
    super(
        HttpStatus.UNPROCESSABLE_ENTITY.value(),
        HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
        errors);
  }
}
