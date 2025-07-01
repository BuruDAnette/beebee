package com.beebee.caronas.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "API de Caronas BeeBee",
        version = "1.0",
        description = "Documentação da API para o sistema de Caronas do BeeBee."
    )
)
public class OpenApiConfig {

}