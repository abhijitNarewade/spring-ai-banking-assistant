package com.banking.ai.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI bankingAssistantOpenApi() {
        return new OpenAPI().info(new Info()
                .title("spring-ai-banking-assistant API")
                .version("1.0.0")
                .description("RAG banking assistant APIs with guardrails, citations, audit logging, and multi-model routing.")
                .contact(new Contact().name("Banking AI Platform Team"))
                .license(new License().name("MIT").url("https://opensource.org/license/mit")));
    }
}
