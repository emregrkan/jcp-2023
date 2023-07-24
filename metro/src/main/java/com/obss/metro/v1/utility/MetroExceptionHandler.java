package com.obss.metro.v1.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obss.metro.v1.dto.MetroExceptionDTO;
import com.obss.metro.v1.exception.MetroError;
import com.obss.metro.v1.exception.impl.ResourceNotFoundException;
import com.obss.metro.v1.exception.impl.ServerException;
import com.obss.metro.v1.exception.impl.UnauthorizedException;
import com.obss.metro.v1.exception.impl.UnprocessableEntityException;
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

// todo: document this
// todo: handle json parse
// todo: remove 422 from unnecessary methods

@RestControllerAdvice
@Slf4j
public class MetroExceptionHandler {
  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public MetroExceptionDTO handleConstraintViolationExceptions(
      final ConstraintViolationException exception) {
    final Set<MetroError> errors =
        exception.getConstraintViolations().parallelStream()
            .map(
                violation ->
                    new MetroError(violation.getPropertyPath().toString(), violation.getMessage()))
            .collect(Collectors.toSet());

    final UnprocessableEntityException ex = new UnprocessableEntityException(errors);
    log.error("", ex);

    return new MetroExceptionDTO(ex);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public MetroExceptionDTO handleResourceNotFoundException(final ResourceNotFoundException ex) {
    log.error("Exception: ", ex);
    return new MetroExceptionDTO(ex);
  }

  @ExceptionHandler(ServerException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public MetroExceptionDTO handleInternalServerError(final ServerException ex) {
    log.error("Exception: ", ex);
    return new MetroExceptionDTO(ex);
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
  public MetroExceptionDTO handleAuthenticationExceptionForOpenAPI() {
    return null;
  }

  @Component
  @RequiredArgsConstructor(onConstructor = @__(@Autowired))
  public static class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    public final ObjectMapper objectMapper;

    @Override
    public void commence(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final AuthenticationException authException)
        throws IOException, ServletException {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.setContentType("application/json");
      final ServletOutputStream outputStream = response.getOutputStream();
      final UnauthorizedException ex = new UnauthorizedException();
      log.error("", ex);
      objectMapper.writeValue(outputStream, new MetroExceptionDTO(ex));
      outputStream.flush();
    }
  }
}
