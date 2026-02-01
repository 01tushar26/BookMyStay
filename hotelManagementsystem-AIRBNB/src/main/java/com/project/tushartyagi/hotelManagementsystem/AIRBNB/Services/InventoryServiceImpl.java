package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.HotelPricingDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.HotelSearchRequest;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.InventoryDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.InventoryUpdateRequestDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.HotelEntity;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Inventory;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.RoomEntity;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.User;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Exceptions.ResourceNotFoundException;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories.HotelMinPriceRepository;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories.InventoryRepositories;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories.RoomRepositories;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    @Autowired
    private final InventoryRepositories inventoryRepo;
    private final HotelMinPriceRepository minPriceRepo;
    private final ModelMapper mapper;
    private final RoomRepositories roomRepo;


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

    @Override
    public List<InventoryDTO> getAllInventoryByRoom(Long id) {
        log.info("Getting the inventories for the room with id {}",id);
        RoomEntity room =roomRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Room not found with id:"+id));
        User user  = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(room.getHotel().getOwner().getId() != user.getId()){
            throw  new AccessDeniedException("Current user not owned this room");

        }

        List<Inventory> inventories = inventoryRepo.findByRoomOrderedByDateDesc(room);

        return inventories
                .stream()
                .map(all->mapper.map(all,InventoryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void UpdateInventoryByRoom(Long id, InventoryUpdateRequestDTO updateRequestDTO) {
        log.info("Updating the inventories for the room with id {} between dates {} to {}",id,updateRequestDTO.getStartDate(),updateRequestDTO.getEndDate());
        RoomEntity room =roomRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Room not found with id:"+id));
        User user  = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(room.getHotel().getOwner().getId() != user.getId()){
            throw  new AccessDeniedException("Current user not owned this room");
        }
        ///before updating make sure to update the inventories
        inventoryRepo.getInventoryAndLockBeforeUpdate(room.getId(),updateRequestDTO.getStartDate(),updateRequestDTO.getEndDate());

        inventoryRepo.updateInventoryByRoom(room.getId(),updateRequestDTO.getSurgeFactor(),updateRequestDTO.getStartDate(),updateRequestDTO.getEndDate(),updateRequestDTO.getClosed());

    }
}
