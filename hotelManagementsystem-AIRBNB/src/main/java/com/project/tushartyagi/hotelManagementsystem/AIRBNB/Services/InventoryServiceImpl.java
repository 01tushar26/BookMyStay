package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.HotelDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.HotelPricingDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.HotelSearchRequest;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.HotelEntity;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Inventory;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.RoomEntity;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Exceptions.ResourceNotFoundException;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories.HotelMinPriceRepository;
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
    private final HotelMinPriceRepository minPriceRepo;
    private final ModelMapper mapper;

    @Override
    public void initializeRoomForAYear(RoomEntity room) {

        log.info("Creating a inventories for room with id {}",room.getId());
        LocalDate today = LocalDate.now();
        LocalDate endDate =today.plusYears(1);
        for(; !today.isAfter(endDate);today=today.plusDays(1)){
            Inventory inventory = Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .date(today)
                    .bookedCount(0)
                    .reservedCount(0)
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
        log.info("Deleting all inventories for the room with id {}",room.getId());
        inventoryRepo.deleteByRoom(room);
    }

    @Override
    public Page<HotelPricingDTO> searchHotels(HotelSearchRequest hotelSearchRequest) {
        log.info("Searching hotels for {} city, from {} to {}",hotelSearchRequest.getCity(),hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate());
        Pageable pageable = PageRequest.of(hotelSearchRequest.getPage(), hotelSearchRequest.getSize());
        Long dayCount = ChronoUnit.DAYS.between(hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate()) +1;

        //buisness logic for first 90days
        Page<HotelPricingDTO> hotels = minPriceRepo.findHotelByAvailableInventory(hotelSearchRequest.getStartDate(),
                hotelSearchRequest.getEndDate(),hotelSearchRequest.getCity(),hotelSearchRequest.getRoomCount(),dayCount,pageable
                                                );

        // can skip this if part because if data is null or not found then page return empty page.....
        if(!hotels.isEmpty()){
           return hotels;
        }

        Page<HotelEntity> hotel = inventoryRepo.findHotelByAvailableInventory(hotelSearchRequest.getStartDate(),
                hotelSearchRequest.getEndDate(),hotelSearchRequest.getCity(),hotelSearchRequest.getRoomCount(),dayCount,pageable
        );

        // can skip this if part because if data is null or not found then page return empty page.....
        if(hotel.isEmpty()){
            throw new ResourceNotFoundException("No hotels available during "+ hotelSearchRequest.getStartDate()+"and"+hotelSearchRequest.getEndDate());

        }
        return hotel.map(h ->
                new HotelPricingDTO(
                        h,
                        null // or compute a rough price if you want
                )
        );

    }
}
