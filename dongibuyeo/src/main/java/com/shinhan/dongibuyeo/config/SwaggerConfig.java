package com.shinhan.dongibuyeo.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Boot 3.0 REST API")
                        .description("Spring Boot 3.0 REST API Documentation")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Your Name")
                                .url("http://your-url.com")
                                .email("your-email@domain.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}
