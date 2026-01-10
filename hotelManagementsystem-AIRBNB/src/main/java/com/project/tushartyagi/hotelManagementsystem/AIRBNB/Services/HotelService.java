package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.HotelDTO;

import java.util.Optional;

public interface HotelService {

    HotelDTO createNewHotel(HotelDTO hotelDTO);
    Optional<HotelDTO> getHotelById(Long id);
}
