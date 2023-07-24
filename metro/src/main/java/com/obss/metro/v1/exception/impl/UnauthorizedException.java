package com.obss.metro.v1.exception.impl;

import com.obss.metro.v1.exception.MetroError;
import com.obss.metro.v1.exception.MetroException;
import java.util.Set;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends MetroException {
  public UnauthorizedException() {
    super(
        HttpStatus.UNAUTHORIZED.value(),
        HttpStatus.UNAUTHORIZED.getReasonPhrase(),
        Set.of(new MetroError("Authorization", "Invalid or missing authorization header")));
  }
}
