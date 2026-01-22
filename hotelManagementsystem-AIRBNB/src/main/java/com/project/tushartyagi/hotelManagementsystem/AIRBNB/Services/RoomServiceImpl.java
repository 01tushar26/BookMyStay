package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.RoomDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.HotelEntity;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.RoomEntity;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.User;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Exceptions.ResourceNotFoundException;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Exceptions.UNauthorisedException;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories.HotelRepositories;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories.RoomRepositories;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepositories roomRepo;
    private final HotelRepositories hotelRepo;
    private final InventoryService inventoryService;
    private final ModelMapper mapper;

    @Override
    @Transactional
    public RoomDTO createNewRoom(Long hotelId, RoomDTO roomDTO) {
        log.info("Creating a room in a hotel with id : "+hotelId);
        HotelEntity hotel = hotelRepo.findById(hotelId).
                orElseThrow(()->new ResourceNotFoundException("Hotel with id : "+hotelId+ " is not found"));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw new UNauthorisedException("This user does not own this hotel with id: "+hotelId+"So cannot able to create the room in this hotel");
        }
        RoomEntity room = mapper.map(roomDTO,RoomEntity.class);
        room.setHotel(hotel);
        roomRepo.save(room);

        if(hotel.getIsActive() == true){
           inventoryService.initializeRoomForAYear(room);
        }

        return mapper.map(room,RoomDTO.class);
    }

    @Override
    public RoomDTO getRoomById(Long id) {
        log.info("Getting a room with id : "+id);
        RoomEntity room = roomRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Room is not found with this id : "+id));
//        HotelEntity hotel = room.getHotel();
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        if(!user.equals(hotel.getOwner())){
//            throw new UNauthorisedException("This user does not own this hotel with id: "+hotel.getId());
//        }
        return mapper.map(room,RoomDTO.class);
    }

    @Override
    public List<RoomDTO> getAllRoomInHotel(Long hotelId) {
        log.info("Getting all the rooms in a hotel with id : "+hotelId);
        HotelEntity hotel = hotelRepo.findById(hotelId).
                orElseThrow(()->new ResourceNotFoundException("Hotel with id : "+hotelId+ " is not found"));
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        if(!user.equals(hotel.getOwner())){
//            throw new UNauthorisedException("This user does not own this hotel with id: "+hotelId);
//        }
        List<RoomEntity> rooms = hotel.getRooms();
        if(rooms == null){
            throw new RuntimeException("No room available at this moment");
        }
        Stream<RoomEntity> s = rooms.stream();

        return  s
                .map(all->mapper.map(all,RoomDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Void deleteRoomById(Long id) {
        log.info("Deleting all room with id : "+id);
        RoomEntity room = roomRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Room is not found with this id : "+id));
        HotelEntity hotel = room.getHotel();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw new UNauthorisedException("This user does not own this room with id: "+id);
        }

        // either used this to maintain db consistency or use cascade simply
        inventoryService.deleteAllInventory(room);
        roomRepo.deleteById(id);
        return null;
    }
}
