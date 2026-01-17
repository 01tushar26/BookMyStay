package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Strategy;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Booking;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Inventory;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories.HolidaysRepository;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services.HolidaysService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;



@RequiredArgsConstructor
public class HolidayPricingStrategy implements PricingStrategy{

    private final PricingStrategy wrapper;
    private final HolidaysService holidaysService;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {


        boolean isHoliday = false;
        LocalDate date = inventory.getDate();

        BigDecimal price = wrapper.calculatePrice(inventory);

        DayOfWeek dayOfWeek = date.getDayOfWeek();



        if(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY){
            isHoliday= true;
        }
        if(holidaysService.existsByHolidayDate(date) == true){
            isHoliday = true;
        }


        if(isHoliday){
            price = price.multiply(BigDecimal.valueOf(1.5));
        }
        return price;
    }
}
