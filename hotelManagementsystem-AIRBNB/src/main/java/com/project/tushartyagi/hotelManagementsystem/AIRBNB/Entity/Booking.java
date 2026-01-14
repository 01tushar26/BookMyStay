package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Enums.BookingStatus;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "hotel_id")
    private HotelEntity hotel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "room_id")
    private RoomEntity room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer roomCount;

    @Column(nullable = false)
    private LocalDate checkInDate;

    @Column(nullable = false)
    private LocalDate checkOutDate;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;


    //owning side
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            //this is how we define our own namimng in new table(otherwise jpa did it by their own
            name = "booking_guest",
            joinColumns = @JoinColumn( name="booking_id"),
            inverseJoinColumns = @JoinColumn(name="guest_id")
    )
    private Set<Guest> guest;

}
