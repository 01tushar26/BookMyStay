package com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class HotelInfo {
    private HotelDTO hotel;
    private List<RoomDTO> rooms;


}
