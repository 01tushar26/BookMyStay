package com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.*;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookingDTO {

    private Long id;
    private Integer roomCount;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    private BookingStatus status;
    private Set<GuestDTO> guest;
    private BigDecimal price;


}
