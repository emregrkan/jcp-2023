package com.obss.metro.v1.exception;

import java.util.Set;

public record ExceptionWrapper(
    Integer status, String error, String message, Set<ExceptionBase> errors) {}
