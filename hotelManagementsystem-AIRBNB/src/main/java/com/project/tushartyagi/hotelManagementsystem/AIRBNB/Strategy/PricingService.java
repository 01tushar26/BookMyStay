package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Strategy;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Inventory;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services.HolidaysService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PricingService {
    private final HolidaysService holidaysService;
    public BigDecimal calculateDynamicPrice(Inventory inventory){
        PricingStrategy pricingStrategy = new BasePricingStrategy();

        pricingStrategy = new SurgePricingStrategy(pricingStrategy);
        pricingStrategy = new OccupancyPricingStrategy(pricingStrategy);
        pricingStrategy = new UrgencyPricingStrategy(pricingStrategy);
        pricingStrategy = new HolidayPricingStrategy(pricingStrategy,holidaysService);

        return pricingStrategy.calculatePrice(inventory);


    }
    public BigDecimal calculateTotalPrice(List<Inventory> inventoryList) {
        return inventoryList.stream()
                .map(this::calculateDynamicPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
