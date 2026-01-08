package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity;


import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class HotelContactInfoEntity {

    private String address;
    private String location;
    private String email;
    private String phoneNumber;

}
