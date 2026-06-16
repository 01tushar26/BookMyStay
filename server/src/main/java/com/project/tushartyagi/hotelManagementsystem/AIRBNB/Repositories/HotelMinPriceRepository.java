package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.HotelPricingDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.HotelEntity;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.HotelMinPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface HotelMinPriceRepository extends JpaRepository<HotelMinPrice, Long> {

    @Query("""
            SELECT new com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.HotelPricingDTO(i.hotel,AVG(i.price))
                FROM HotelMinPrice i
                WHERE i.hotel.city = :city
                  AND i.date BETWEEN :startDate AND :endDate
                  AND i.hotel.isActive = true
                GROUP BY i.hotel
            """)
    Page<HotelPricingDTO> findHotelByAvailableInventory(
            @Param("startDate") LocalDate startDate,
            @Param("endDate")LocalDate endDate,
            @Param("city")String city,

            @Param("roomCount") Integer roomCount,
            @Param("dayCount") Long dayCount,
            Pageable pageable
    );



    Optional<HotelMinPrice> findByHotelAndDate(HotelEntity hotelEntity, LocalDate date);
}