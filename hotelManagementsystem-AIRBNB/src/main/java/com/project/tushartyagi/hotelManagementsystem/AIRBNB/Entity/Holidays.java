package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "list_of_holidays")
@AllArgsConstructor
@NoArgsConstructor
public class Holidays {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name ;

    @Column(nullable = false)
    private LocalDate holidayDate;

    private String countryCode="IN";

}
