package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Controller;


import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.BookingDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.BookingRequest;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.GuestDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class HotelBookingController {

    private final BookingService bookingService;
    @PostMapping("/init")
    public ResponseEntity<BookingDTO> initializeBooking(@RequestBody BookingRequest bookingRequest){
     return  ResponseEntity.ok(bookingService.initializeBooking(bookingRequest));

    }
    @PostMapping("/{bookingId}/addGuests")
    public ResponseEntity<BookingDTO> addGuests(@RequestBody List<GuestDTO> guests,
                                                @PathVariable (name = "bookingId") Long id ){
        return  ResponseEntity.ok(bookingService.addGuest(guests,id));

    }
}
