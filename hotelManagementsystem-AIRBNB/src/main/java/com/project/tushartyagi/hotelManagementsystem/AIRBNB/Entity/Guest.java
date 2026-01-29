package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Enums.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private String name;

    private Integer age;

    //Inverse side
    @ManyToMany(mappedBy = "guest",fetch = FetchType.LAZY)
    private Set<Booking> bookings;

}
