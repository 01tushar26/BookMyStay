package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.BookingDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.UserDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.UserUpdateRequestDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.User;

import java.util.List;

public interface UserService {

    User getUserById(Long id);

    UserDTO updateProfile(UserUpdateRequestDTO updateRequestDTO);

    List<BookingDTO> getAllMyBooking();

    UserDTO getMyProfile();
}
