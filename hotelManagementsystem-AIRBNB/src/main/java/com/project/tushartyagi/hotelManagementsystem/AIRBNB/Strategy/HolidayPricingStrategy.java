package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Strategy;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Booking;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@RequiredArgsConstructor
public class HolidayPricingStrategy implements PricingStrategy{

    private final PricingStrategy wrapper;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapper.calculatePrice(inventory);
        Boolean isHoliday = true ; //Todo : add the api to give back whether there is an holiday on booking day or not OR u can simply used the Array that comtain all the holidays
        if(isHoliday){
            price = price.multiply(BigDecimal.valueOf(1.5));
        }
        return price;
    }
}
