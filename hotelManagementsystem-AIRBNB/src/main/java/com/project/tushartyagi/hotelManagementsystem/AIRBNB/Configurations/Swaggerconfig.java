package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Configurations;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Swaggerconfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("BookMyStay")
                                .version("1.0")
                                .description(
                                        "Production-ready backend APIs for a hotel booking platform. " +
                                                "Supports JWT authentication, hotel management, room inventory, bookings, " +
                                                "payments, reporting, and user profile management."
                                )
                                .contact(
                                        new Contact()
                                                .name("Tushar Tyagi")
                                                .email("tushartyagi2601@gmail.com")
                                                .url("https://tushardev.me")
                                )

                )
                .externalDocs(
                        new ExternalDocumentation()
                                .description("GitHub Repository")
                                .url("https://github.com/01tushar26/BookMyStay")
                )


                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
        //instead of making the all api locked use @SecurityRequirement(name = "bearerAuth") before controller
//                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}