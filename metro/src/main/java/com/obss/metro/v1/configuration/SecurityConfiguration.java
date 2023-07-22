package com.obss.metro.v1.configuration;

import com.obss.metro.v1.utility.MetroExceptionHandler;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.CrossOrigin;

@Configuration
@CrossOrigin(origins = "http://localhost:3000")
@SecurityScheme(
    name = SecurityConfiguration.SECURITY_CONFIG_NAME,
    in = SecuritySchemeIn.HEADER,
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityConfiguration {

  public static final String SECURITY_CONFIG_NAME = "App Bearer Token";
  private static final String[] RESOURCE_WHITE_LIST = {"/swagger-ui/**", "/v3/api-docs/**"};

  private final MetroExceptionHandler.RestAuthenticationEntryPoint authenticationEntryPoint;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            // todo: look for more secure ways
            auth ->
                auth.requestMatchers(RESOURCE_WHITE_LIST).permitAll().anyRequest().authenticated())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .oauth2ResourceServer(
            oauth2 ->
                oauth2
                    .jwt(Customizer.withDefaults())
                    .authenticationEntryPoint(authenticationEntryPoint));

    return http.build();
  }
}
