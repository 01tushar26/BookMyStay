package com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateRequestDTO {
    private String name;
    private LocalDate dateOfBirth;
    private Gender gender;
}
