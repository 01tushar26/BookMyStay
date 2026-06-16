package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.BookingDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.BookingRequest;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.GuestDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.HotelReportDTO;
import com.stripe.model.Event;
import org.jspecify.annotations.Nullable;


import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    BookingDTO initializeBooking(BookingRequest bookingRequest);

    BookingDTO addGuest(List<GuestDTO> guests, Long id);

    String initiatePayments(Long id);

    void capturePayment(Event event);

    void cancelBooking(Long id);

    List<BookingDTO> getAllTheBookingOfParticularHotel(Long id);

    HotelReportDTO getHotelReport(Long id, LocalDate startDate, LocalDate endDate);
}
