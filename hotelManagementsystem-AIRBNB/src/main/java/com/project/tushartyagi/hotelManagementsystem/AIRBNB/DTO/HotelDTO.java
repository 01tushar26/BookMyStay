package com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.HotelContactInfoEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

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
