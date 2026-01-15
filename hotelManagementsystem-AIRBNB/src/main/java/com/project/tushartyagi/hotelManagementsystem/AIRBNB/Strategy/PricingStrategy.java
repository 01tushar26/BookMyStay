package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Strategy;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Inventory;

import java.math.BigDecimal;

public interface PricingStrategy {

    BigDecimal calculatePrice(Inventory inventory);
}
