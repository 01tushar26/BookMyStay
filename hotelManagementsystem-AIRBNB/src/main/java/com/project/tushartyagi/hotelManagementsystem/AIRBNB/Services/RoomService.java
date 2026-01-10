package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.RoomDTO;

import java.util.List;

public interface RoomService {
    RoomDTO createNewRoom(Long hotelId, RoomDTO roomDTO);

    RoomDTO getRoomById(Long id);

    List<RoomDTO> getAllRoomInHotel(Long HotelId);

    Void deleteRoomById(Long id);


}
