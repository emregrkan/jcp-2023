package com.obss.metro.v1.exception;

import java.io.Serial;
import java.io.Serializable;

public record MetroError(String field, String message) implements Serializable {
  @Serial private static final long serialVersionUID = -7076790028821250050L;
}
