package com.obss.metro.v1.configuration;

import com.obss.metro.v1.utility.MetroExceptionHandler;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@SecurityScheme(
    name = SecurityConfiguration.SECURITY_CONFIG_NAME,
    in = SecuritySchemeIn.HEADER,
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class SecurityConfiguration {

  public static final String SECURITY_CONFIG_NAME = "App Bearer Token";
  private static final String[] RESOURCE_WHITE_LIST = {"/swagger-ui/**", "/v3/api-docs/**"};

  private final MetroExceptionHandler.RestAuthenticationEntryPoint authenticationEntryPoint;
  private final MetroExceptionHandler.RestForbiddenHandler forbiddenHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            // todo: look for more secure ways
            auth ->
                auth.requestMatchers(RESOURCE_WHITE_LIST).permitAll().anyRequest().authenticated())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .oauth2ResourceServer(
            oauth2 ->
                oauth2
                    .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                    .accessDeniedHandler(forbiddenHandler)
                    .authenticationEntryPoint(authenticationEntryPoint));

    return http.build();
  }

  // todo: should ask this
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
    return source;
  }

  @SuppressWarnings("unchecked")
  public Converter<Jwt, Collection<GrantedAuthority>> authoritiesConverter() {
    return jwt -> {
      final var realmAccess =
          (Map<String, Collection<String>>)
              (jwt.getClaims().getOrDefault("realm_access", Collections.emptySet()));
      return realmAccess.getOrDefault("roles", Collections.emptySet()).stream()
          .map(SimpleGrantedAuthority::new)
          .collect(Collectors.toSet());
    };
  }

  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {
    final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter());
    return jwtAuthenticationConverter;
  }
}
