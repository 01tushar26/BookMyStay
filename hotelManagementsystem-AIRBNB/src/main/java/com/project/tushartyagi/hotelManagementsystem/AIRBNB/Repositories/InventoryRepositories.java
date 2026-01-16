package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.HotelEntity;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Inventory;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.RoomEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InventoryRepositories extends JpaRepository<Inventory,Long> {

    void deleteByRoom(RoomEntity room);


    @Query("""
            SELECT DISTINCT i.hotel
                FROM Inventory i
                WHERE i.city = :city
                  AND i.date BETWEEN :startDate AND :endDate
                  AND i.closed = false
                  AND (i.totalCount - i.bookedCount-i.reservedCount) >= :roomCount
                GROUP BY i.hotel, i.room
                HAVING COUNT(i.date) = :dayCount
            """)
    Page<HotelEntity> findHotelByAvailableInventory(
            @Param("startDate")LocalDate startDate,
            @Param("endDate")LocalDate endDate,
            @Param("city")String city,

            @Param("roomCount") Integer roomCount,
            @Param("dayCount") Long dayCount,
            Pageable pageable
    );

    @Query("""
            SELECT  i
                FROM Inventory i
                WHERE i.room.id = :roomId
                  AND i.date BETWEEN :startDate AND :endDate
                  AND i.closed = false
                  AND (i.totalCount - i.bookedCount - i.reservedCount) >= :roomCount
            
            """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Inventory> findAndLockAvailableInventories(
            @Param("startDate")LocalDate startDate,
            @Param("endDate")LocalDate endDate,
            @Param("roomCount") Integer roomCount,
            @Param("roomId") Long roomId
    );

    List<Inventory> findByHotelAndDateBetween(HotelEntity hotelEntity, LocalDate startDate, LocalDate endDate);
}
