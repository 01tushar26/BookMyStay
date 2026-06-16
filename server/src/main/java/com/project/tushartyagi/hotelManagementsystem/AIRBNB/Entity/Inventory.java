package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table( uniqueConstraints = @UniqueConstraint(
        name = "unique_hotel_room_date",
        columnNames ={ "hotel_id","room_id","date"}
))
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "hotel_id")
    private HotelEntity hotel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "room_id")
    private RoomEntity room;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Integer totalCount;

    @Column(nullable = false , columnDefinition = "INTEGER DEFAULT 0")
    private Integer bookedCount=0;

    @Column(nullable = false , columnDefinition = "INTEGER DEFAULT 0")
    private Integer reservedCount=0;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;

    @Column(nullable = false ,precision = 5,scale = 2)
    private BigDecimal surgeFactor;

    @Column(nullable = false ,precision = 10,scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Boolean closed;

    @Column(nullable = false)
    private String city;

}
