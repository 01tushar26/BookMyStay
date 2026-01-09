package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Configurations;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper getModel(){
        return new ModelMapper();
    }
}
