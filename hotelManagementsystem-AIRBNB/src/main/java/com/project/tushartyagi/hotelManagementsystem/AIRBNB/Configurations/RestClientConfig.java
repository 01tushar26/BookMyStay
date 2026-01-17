package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient getRestClient(){
       return RestClient.builder()
               .baseUrl("https://calendarific.com/api/v2")
               .build();
    }
}
