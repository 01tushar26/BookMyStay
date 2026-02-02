package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Controller;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.BookingDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.UserDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.UserUpdateRequestDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping(path = "/profile")
    public ResponseEntity<UserDTO> updateProfile(@RequestBody UserUpdateRequestDTO updateRequestDTO){

        UserDTO profileDTO=userService.updateProfile(updateRequestDTO);
        return ResponseEntity.ok(profileDTO);
    }
    @GetMapping(path = "/myBookings")
    public ResponseEntity<List<BookingDTO>> getAllMyBooking(){

        List<BookingDTO> bookingList=userService.getAllMyBooking();
        return ResponseEntity.ok(bookingList);
    }
    @GetMapping(path = "/profile")
    public ResponseEntity<UserDTO> getMyProfile(){

       UserDTO bookingList=userService.getMyProfile();
        return ResponseEntity.ok(bookingList);
    }
}
