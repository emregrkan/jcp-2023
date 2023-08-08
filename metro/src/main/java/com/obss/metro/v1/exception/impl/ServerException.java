package com.obss.metro.v1.exception.impl;

import com.obss.metro.v1.exception.MetroError;
import com.obss.metro.v1.exception.MetroException;
import java.util.Set;
import org.springframework.http.HttpStatus;

public class ServerException extends MetroException {

  public ServerException() {
    super(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
        Set.of(new MetroError("Server", "Server faced an issue")));
  }
}
