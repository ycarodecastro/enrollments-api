package com.example.projectapi.security.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI configApi(){
        return new OpenAPI()
                .info(
                        // informações principais;
                        new Info()
                                .title("api-enrollments")
                                .description("API para gerenciamento de ofertas de vagas, alunos e escolas.")
                                .version("1")
                        )
                .components(
                        // sistema serve para criar o cadeado do swagger;
                        new Components()
                                .addSecuritySchemes(
                                        // CHAVE do componente do cadeado;
                                        "bearer-key",
                                        new SecurityScheme()

                                                // tipo da schema da requisição;
                                                .type(SecurityScheme.Type.HTTP)

                                                // schema que vai receber;
                                                .scheme("bearer")

                                                // formato;
                                                .bearerFormat("JWT")
                                )
                );

    }

}
