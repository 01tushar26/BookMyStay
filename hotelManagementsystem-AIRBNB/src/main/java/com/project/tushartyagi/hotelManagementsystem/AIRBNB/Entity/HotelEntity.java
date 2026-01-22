package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity // not recommended used getters and setters instead
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "hotels")
public class HotelEntity {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false ,unique = true)
    private String name;

    private String city;

    @Column(columnDefinition = "TEXT[]")
    private String[] photos;

    @Column(columnDefinition = "TEXT[]")
    private String[] amenities;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Embedded
    private HotelContactInfoEntity hotelContactInfo;

    @Column(nullable = false)
    private Boolean isActive;

    //owner side
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private User owner;

    //inverse side
    @JsonIgnore
    @OneToMany(mappedBy = "hotel",fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    List<RoomEntity> rooms;

    @JsonIgnore
    @OneToMany(mappedBy = "hotel",fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    List<Inventory> inventories;


}
