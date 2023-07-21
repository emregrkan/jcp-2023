package com.obss.metro.v1.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// todo: fix this
@Configuration
@OpenAPIDefinition(security = @SecurityRequirement(name = "bearer-token"))
public class OpenAPIConfiguration {
  @Bean
  public OpenApiCustomizer addBearerTokenScheme() {
    return openApi ->
        openApi
            .getComponents()
            .addSecuritySchemes(
                "Access Token",
                new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT"));
  }
}
