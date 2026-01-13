package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.HotelDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.HotelSearchRequest;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.HotelEntity;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Inventory;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.RoomEntity;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Exceptions.ResourceNotFoundException;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories.InventoryRepositories;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    @Autowired
    private final InventoryRepositories inventoryRepo;
    private final ModelMapper mapper;

    @Override
    public void initializeRoomForAYear(RoomEntity room) {
        LocalDate today = LocalDate.now();
        LocalDate endDate =today.plusYears(1);
        for(; !today.isAfter(endDate);today=today.plusDays(1)){
            Inventory inventory = Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .date(today)
                    .bookedCount(0)
                    .totalCount(room.getTotalCount())
                    .surgeFactor(BigDecimal.ONE)
                    .price(room.getBasePrice())
                    .closed(false)
                    .city(room.getHotel().getCity())
                    .build();
            inventoryRepo.save(inventory);

        }
    }

    @Override
    public void deleteAllInventory(RoomEntity room) {
        inventoryRepo.deleteByRoom(room);
    }

    @Override
    public Page<HotelDTO> searchHotels(HotelSearchRequest hotelSearchRequest) {
        Pageable pageable = PageRequest.of(hotelSearchRequest.getPage(), hotelSearchRequest.getSize());
        Long roomCount = ChronoUnit.DAYS.between(hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate()) +1;
        Page<HotelEntity> hotels = inventoryRepo.findHotelByAvailableInventory(hotelSearchRequest.getStartDate(),
                hotelSearchRequest.getEndDate(),hotelSearchRequest.getCity(),hotelSearchRequest.getRoomCount(),roomCount,pageable
                                                );

        // can skip this if part because if data is null or not found then page return empty page.....
        if(hotels.isEmpty()){
            throw new ResourceNotFoundException("No hotels available during "+ hotelSearchRequest.getStartDate()+"and"+hotelSearchRequest.getEndDate());

        }

        return hotels.map(all->mapper.map(all,HotelDTO.class));
    }
}
