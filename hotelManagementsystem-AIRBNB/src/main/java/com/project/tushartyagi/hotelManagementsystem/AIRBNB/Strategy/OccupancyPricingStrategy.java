package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Strategy;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@RequiredArgsConstructor
public class OccupancyPricingStrategy implements PricingStrategy{
    private final PricingStrategy wrapper;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapper.calculatePrice(inventory);
        Double rate = (double) (inventory.getBookedCount() / inventory.getTotalCount());
        if(rate >0.8){
            price = price.multiply(BigDecimal.valueOf(1.2));
        }
        return price;
    }
}
