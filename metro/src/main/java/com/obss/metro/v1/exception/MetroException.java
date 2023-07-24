package com.obss.metro.v1.exception;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class MetroException extends RuntimeException implements Serializable {
  @Serial private static final long serialVersionUID = -5239451240756292416L;
  private final Integer status;
  private final String message;
  private final Set<MetroError> errors;
}
