package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.BookingDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.BookingRequest;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.GuestDTO;


import java.util.List;

public interface BookingService {
    BookingDTO initializeBooking(BookingRequest bookingRequest);

    BookingDTO addGuest(List<GuestDTO> guests, Long id);
}
