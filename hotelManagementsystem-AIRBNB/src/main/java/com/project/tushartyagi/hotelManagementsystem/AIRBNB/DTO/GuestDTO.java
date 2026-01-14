package com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Booking;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Enums.Gender;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
public class GuestDTO {

    private Long id;
    private User user;
    private Gender gender;
    private String name;
    private Integer age;
}
