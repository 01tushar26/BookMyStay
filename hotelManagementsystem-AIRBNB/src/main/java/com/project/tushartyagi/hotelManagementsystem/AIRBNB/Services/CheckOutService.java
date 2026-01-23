package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Booking;

public interface CheckOutService {
    String getCheckoutSession(Booking booking, String sucessUrl, String failureUrl);
}
