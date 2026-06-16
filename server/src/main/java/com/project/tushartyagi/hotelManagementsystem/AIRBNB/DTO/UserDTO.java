package com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDTO {
    private String email;
    private String name;
    private Long id;
    private Gender gender;
    private LocalDate dateOfBirth;
}
