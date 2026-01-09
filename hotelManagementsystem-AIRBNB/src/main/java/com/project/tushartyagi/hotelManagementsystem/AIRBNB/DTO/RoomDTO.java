package com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.HotelEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RoomDTO {

    private Long id;

    //basically we set the fetch type of Hotel is Lazy in RoomEntity

//    @JsonIgnore
//    private HotelEntity hotel;

    private String type;
    private String[] photos;
    private String[] amenities;
    private BigDecimal basePrice;
    private Integer totalCount;
    private Integer capacity;
}
