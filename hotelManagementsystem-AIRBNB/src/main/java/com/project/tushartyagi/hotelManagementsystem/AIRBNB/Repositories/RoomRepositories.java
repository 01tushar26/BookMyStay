package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepositories extends JpaRepository<RoomEntity,Long> {
    Optional<RoomEntity> findByIdAndHotelId(Long roomId, Long hotelId);

}
