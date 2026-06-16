package com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.HotelEntity;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.RoomEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class InventoryDTO {

    private Long id;
    private LocalDate date;
    private Integer totalCount;
    private Integer bookedCount;
    private Integer reservedCount;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    private BigDecimal surgeFactor;
    private BigDecimal price;
    private Boolean closed;



}
