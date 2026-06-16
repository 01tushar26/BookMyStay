package com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HotelSearchRequest {
    private String city;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer roomCount;

    private Integer page=0;
    private Integer size=10;

}
