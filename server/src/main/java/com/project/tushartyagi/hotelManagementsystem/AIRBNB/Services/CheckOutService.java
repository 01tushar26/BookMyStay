package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Booking;

public interface CheckOutService {

    String getCheckoutSession(String successUrl,String failureUrl,Booking booking);

}
