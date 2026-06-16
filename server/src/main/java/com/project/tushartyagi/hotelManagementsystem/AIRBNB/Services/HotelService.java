package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.HotelDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.HotelInfo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface HotelService {
    HotelDTO createNewHotel(HotelDTO hotelDTO);

    HotelDTO getHotelById(Long id);

    HotelDTO updateHotelById(Long id , HotelDTO hotelDto);

    Void deleteHotelById(Long id);

    HotelDTO activateHotelById(Long id);

    HotelInfo getHotelInfoById(Long id);
    List<HotelDTO> getAllTheHotel();
}
