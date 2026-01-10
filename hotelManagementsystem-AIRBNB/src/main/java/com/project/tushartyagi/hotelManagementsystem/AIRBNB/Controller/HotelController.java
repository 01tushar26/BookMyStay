package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Controller;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.HotelDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Exceptions.ResourceNotFoundException;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/hotels")
@Slf4j
@RequiredArgsConstructor
public class HotelController {

    @Autowired
    private final HotelService hotelService;

    @PostMapping
    public ResponseEntity<HotelDTO> createNewHotel(
            @RequestBody HotelDTO hotelDTO
    ){
        HotelDTO hotel = hotelService.createNewHotel(hotelDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(hotel);
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelDTO> getHotelById(@PathVariable (name = "hotelId") Long id){
      HotelDTO hotel = hotelService.getHotelById(id).orElseThrow(()->new  ResourceNotFoundException("No hotel found with this :"+id));
      return ResponseEntity.ok(hotel);
    }

}
