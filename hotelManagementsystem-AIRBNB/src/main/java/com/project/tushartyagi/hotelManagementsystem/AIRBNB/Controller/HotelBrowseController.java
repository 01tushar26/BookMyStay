package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Controller;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.HotelDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.HotelInfo;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.HotelSearchRequest;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services.HotelService;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hotels")
public class HotelBrowseController {

    private final InventoryService inventoryService;
    private final HotelService hotelService;

    @GetMapping("/search")
    public ResponseEntity<Page<HotelDTO>> searchHotels(@RequestBody HotelSearchRequest hotelSearchRequest){
        Page<HotelDTO> res = inventoryService.searchHotels(hotelSearchRequest);
        return ResponseEntity.ok(res);
    }
    @GetMapping("/{hotelId}/info")
    public ResponseEntity<HotelInfo> getHotelInfo(@PathVariable (name = "hotelId")Long id){
        HotelInfo res = hotelService.getHotelInfoById(id);
        return ResponseEntity.ok(res);
    }
}
