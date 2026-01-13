package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Controller.HotelBrowseController;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.HotelDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.HotelSearchRequest;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.RoomEntity;
import org.springframework.data.domain.Page;

public interface InventoryService {
    void initializeRoomForAYear(RoomEntity room);
    void deleteAllInventory(RoomEntity room);

    Page<HotelDTO> searchHotels(HotelSearchRequest hotelSearchRequest);
}
