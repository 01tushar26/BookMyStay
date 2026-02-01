package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Controller.HotelBrowseController;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.*;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.RoomEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InventoryService {
    void initializeRoomForAYear(RoomEntity room);
    void deleteAllInventory(RoomEntity room);

    Page<HotelPricingDTO> searchHotels(HotelSearchRequest hotelSearchRequest);

    List<InventoryDTO> getAllInventoryByRoom(Long id);


    void UpdateInventoryByRoom(Long id, InventoryUpdateRequestDTO updateRequestDTO);
}
