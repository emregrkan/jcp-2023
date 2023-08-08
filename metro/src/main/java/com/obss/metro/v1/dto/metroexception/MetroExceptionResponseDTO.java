package com.obss.metro.v1.dto.metroexception;

import com.obss.metro.v1.exception.MetroError;
import com.obss.metro.v1.exception.MetroException;
import java.util.Set;

// This DTO returned to front-end
public record MetroExceptionResponseDTO(Integer status, String message, Set<MetroError> errors) {
  public MetroExceptionResponseDTO(final MetroException exception) {
    this(exception.getStatus(), exception.getMessage(), exception.getErrors());
  }
}
