package com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.HotelContactInfoEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class HotelDTO {

    private Long id;

    @NotEmpty(message = "Name should be required")
    private String name;
    private String city;
    private String[] photos;
    private String[] amenities;

    private HotelContactInfoEntity hotelContactInfo;
    private Boolean isActive;

}
