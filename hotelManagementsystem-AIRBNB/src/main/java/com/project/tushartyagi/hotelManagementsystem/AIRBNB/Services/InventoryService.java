package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.RoomEntity;

public interface InventoryService {
    void initializeRoomForAYear(RoomEntity room);
    void deleteAllInventory(RoomEntity room);
}
