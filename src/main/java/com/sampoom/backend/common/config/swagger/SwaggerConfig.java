package com.sampoom.backend.common.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        // JWT Bearer 인증 스킴 정의
        SecurityScheme bearerAuthScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("Authorization");

        // 보안 요구사항 정의
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("BearerAuth");

        Server localServer = new Server()
                .url("http://localhost:8080/")
                .description("로컬 서버");

        Server prodServer = new Server()
                .url("https://sampoom.store/api/agency")
                .description("배포 서버");

        return new OpenAPI()
                .info(new Info()
                        .title("삼삼오토 Agency Service API")
                        .description("Agency 서비스 REST API 문서")
                        .version("1.0.0"))
                .servers(List.of(prodServer, localServer))
                .components(new Components()
                        .addSecuritySchemes("BearerAuth", bearerAuthScheme))
                .addSecurityItem(securityRequirement);
    }
}
