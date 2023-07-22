package com.obss.metro.v1.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obss.metro.v1.exception.ExceptionBase;
import com.obss.metro.v1.exception.ExceptionWrapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

// todo: not sure if this is the right way
// todo: document this
// todo: handle json parse
// todo: remove 422 from unnecessary methods

@RestControllerAdvice
@Slf4j
public class MetroExceptionHandler {
  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public ExceptionWrapper handleConstraintViolationExceptions(
      final ConstraintViolationException exception) {
    final Set<ExceptionBase> errors =
        exception.getConstraintViolations().parallelStream()
            .map(
                violation ->
                    new ExceptionBase(
                        violation.getPropertyPath().toString(), violation.getMessage()))
            .collect(Collectors.toSet());

    log.info(exception.getMessage());

    return new ExceptionWrapper(
        HttpStatus.UNPROCESSABLE_ENTITY.value(),
        "Unprocessable Entity",
        HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
        errors);
  }

  /**
   * <strong>This method is never reached</strong> but used for OpenAPI docs.
   *
   * @return null always
   * @see MetroExceptionHandler.RestAuthenticationEntryPoint
   * @author <a href="mailto:emre-gurkan@hotmail.com">Emre Gürkan</a>
   */
  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ExceptionWrapper handleAuthenticationExceptionForOpenAPI() {
    return null;
  }

  // todo: ask about this
  @Component
  @RequiredArgsConstructor(onConstructor = @__(@Autowired))
  public static class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    public final ObjectMapper objectMapper;

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException)
        throws IOException, ServletException {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.setContentType("application/json");
      final ExceptionBase error =
          new ExceptionBase("Authorization", "Invalid or missing authorization header");
      final ExceptionWrapper errorResponse =
          new ExceptionWrapper(
              HttpStatus.UNAUTHORIZED.value(),
              "Unauthorized",
              "You are not authorized to access this resource",
              Set.of(error));
      final ServletOutputStream outputStream = response.getOutputStream();

      objectMapper.writeValue(outputStream, errorResponse);
      outputStream.flush();
    }
  }
}
