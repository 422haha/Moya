package com.e22e.moya.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@OpenAPIDefinition(
    servers = {
        @Server(url = "https://j11d202.p.ssafy.io", description = "이게모야 HTTPS 서버"),
        @Server(url = "http://j11d202.p.ssafy.io", description = "이게모야 HTTP 서버"),
        @Server(url = "http://localhost:8080", description = "이게모야 Local 서버")
    }
)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components())
            .info(new Info()
                .title("Moya API Documentation")
                .version("1.0")
                .description("이게모야 API 명세서"));
    }
}
