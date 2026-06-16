package com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.HotelEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class HotelPricingDTO {
    private HotelEntity hotel;
    private BigDecimal price ;

    public HotelPricingDTO(HotelEntity hotel, Double  price) {
        this.hotel = hotel;
        this.price = BigDecimal.valueOf(price);
    }
}
