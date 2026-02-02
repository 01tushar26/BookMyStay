package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Controller;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.BookingDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.HotelDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.HotelReportDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Exceptions.ResourceNotFoundException;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services.BookingService;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/hotels")
@Slf4j
@RequiredArgsConstructor
public class HotelController {

    @Autowired
    private final HotelService hotelService;
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<HotelDTO> createNewHotel(
            @RequestBody HotelDTO hotelDTO
    ){
        HotelDTO hotel = hotelService.createNewHotel(hotelDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(hotel);
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelDTO> getHotelById(@PathVariable (name = "hotelId") Long id){
      HotelDTO hotel = hotelService.getHotelById(id);
      return ResponseEntity.ok(hotel);
    }
    @GetMapping()
    public ResponseEntity<List<HotelDTO>> getAllTheHotels(){
      List<HotelDTO> hotel = hotelService.getAllTheHotel();
      return ResponseEntity.ok(hotel);
    }

    //patch is not used in production to update many feild at a tym ...
    @PutMapping("/{hotelId}")
    public ResponseEntity<HotelDTO> updateHotelById(@PathVariable (name = "hotelId") Long id, @RequestBody HotelDTO updates){
        HotelDTO h = hotelService.updateHotelById(id ,updates);
        return ResponseEntity.ok(h);
    }
    //basically this api is used to activate the hotel or u can change the path to a /activate/{hotelId}
    @PatchMapping("/{hotelId}")
    public ResponseEntity<HotelDTO> activateHotel(@PathVariable (name = "hotelId") Long id){
        HotelDTO h = hotelService.activateHotelById(id);
        return ResponseEntity.ok(h);
    }
    @DeleteMapping("/{hotelId}")
    public ResponseEntity<Void> deleteHotelById(@PathVariable (name = "hotelId") Long id){
         hotelService.deleteHotelById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{hotelId}/bookings")
    public ResponseEntity<List<BookingDTO>> getAllTheBookingOfParticularHotel(@PathVariable (name = "hotelId") Long id){

        return ResponseEntity.ok(bookingService.getAllTheBookingOfParticularHotel(id));
    }
    @GetMapping("/{hotelId}/report")
    public ResponseEntity<HotelReportDTO> getHotelReport(@PathVariable (name = "hotelId") Long id,
                                                         @RequestParam (required = false) LocalDate startDate,
                                                         @RequestParam (required = false) LocalDate endDate){


        if(startDate == null){
            startDate = LocalDate.now().minusMonths(1);
        }
        if(endDate == null){
            endDate = LocalDate.now();
        }
        return ResponseEntity.ok(bookingService.getHotelReport(id,startDate,endDate));
    }



}
