package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Controller;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.TestcontainerConfigurations;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureWebTestClient
@Import(TestcontainerConfigurations.class)
@ImportAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class HotelControllerTestIT {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void createNewHotel() {

    }
}