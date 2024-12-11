package com.touchdown.perflowbackend.config;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME, createSecurityScheme()))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .info(apiInfo());
    }

    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
                .name("Authorization") // 헤더 이름
                .type(SecurityScheme.Type.HTTP) // HTTP 기반 인증
                .scheme("bearer") // Bearer 토큰 사용
                .bearerFormat("JWT"); // JWT 형식
    }

    private Info apiInfo() {
        return new Info()
                .title("Perflow Backend API")
                .description("API Swagger 테스트")
                .version("1.0.0");
    }
}
