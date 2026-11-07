package com.petsalud.backendpetsalud.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

// Configura Swagger OpenAPI con autenticación JWT en Spring Boot
@Configuration
public class OpenApiConfig {

      private static final String SCHEME = "bearerAuth";

  @Bean
  public OpenAPI openApi() {
    return new OpenAPI()
      .info(new Info().title("Backend Pet Salud API").version("0.0.1")) // Pasa al Sawgger el nombre y version
      .addSecurityItem(new SecurityRequirement().addList(SCHEME)) // Valida Esquemas
      .components(new Components().addSecuritySchemes(SCHEME, // Valida componentes
        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"))); // Configura el esquema de seguridad 
  }

}
