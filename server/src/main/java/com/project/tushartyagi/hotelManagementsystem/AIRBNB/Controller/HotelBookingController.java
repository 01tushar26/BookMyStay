package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Controller;


import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.BookingDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.BookingRequest;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.GuestDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services.BookingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(
        name = "Booking APIs",
        description = "APIs for hotel room booking, guest management, payment initiation, and booking cancellation operations."
)
@RestController
@SecurityRequirement(name = "bearerAuth")
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
    @PostMapping("/{bookingId}/payments")
    public ResponseEntity<Map<String,String>> payments(@PathVariable (name = "bookingId") Long id ){
        String sessionUrl = bookingService.initiatePayments(id);
        return  ResponseEntity.ok(Map.of("Session Url",sessionUrl));

    }
    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable (name = "bookingId") Long id ){
        bookingService.cancelBooking(id);
        return  ResponseEntity.noContent().build();

    }
}
