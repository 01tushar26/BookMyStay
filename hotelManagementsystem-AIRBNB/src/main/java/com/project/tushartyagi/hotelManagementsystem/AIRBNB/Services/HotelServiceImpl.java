package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services;


import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.HotelDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.HotelInfo;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.RoomDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.HotelEntity;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.RoomEntity;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Exceptions.AlreadyExistException;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Exceptions.ResourceNotFoundException;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories.HotelRepositories;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories.RoomRepositories;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final RoomRepositories roomRepositories;

    private final HotelRepositories hotelRepo;
    private final InventoryService inventoryService;
    private final RoomService roomService;
    private final ModelMapper mapper;

    @Override
    public HotelDTO createNewHotel(HotelDTO hotelDTO) {
        log.info("Creating a new hotel with name : {}", hotelDTO.getName());
        Boolean isExistbyname = hotelRepo.existsByName( hotelDTO.getName());
        if(isExistbyname == true){

            throw new AlreadyExistException("Hotel with this name: "+hotelDTO.getName()+" already Exist");
        }
        HotelEntity tobeCreatedHotel = mapper.map(hotelDTO,HotelEntity.class);
        tobeCreatedHotel.setIsActive(false);
       HotelEntity newHotel = hotelRepo.save(tobeCreatedHotel);
        log.info("New Hotel is Created with id : {}", newHotel.getId());

        return mapper.map(newHotel,HotelDTO.class);
    }

    @Override
    public Optional<HotelDTO> getHotelById(Long id) {

        log.info("Getting hotel with id :{}",id);

        Optional<HotelEntity> hotel = hotelRepo.findById(id);

        return hotel.map(all->mapper.map(all,HotelDTO.class));

    }

    @Override
    public HotelDTO updateHotelById(Long id, HotelDTO hotelDTO) {
        log.info("Updating a hotel with id :{}",id);
        HotelEntity hotel = hotelRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Hotel with id : "+id+ " is not found"));
        mapper.map(hotelDTO,hotel);
        hotel.setId(id);

        //will doif this is a patch request

//        hotelDTO.forEach((feildName,value)->{
//            Field field = ReflectionUtils.getRequiredField(HotelEntity.class,feildName);
//
////            if (field == null) {
////                throw new IllegalArgumentException("Invalid field: " + feildName);
////            }
//
//            field.setAccessible(true);
//            ReflectionUtils.setField(field, hotel, value);
//        });

        HotelEntity h2= hotelRepo.save(hotel);
        return mapper.map(h2,HotelDTO.class);
    }

    @Override
    @Transactional
    public Void deleteHotelById(Long id) {
        log.info("Deleting the hotel with id : "+id);
        HotelEntity hotel = hotelRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Hotel with id : "+id+ " is not found"));
        // either use this for loop logic or use cascade simply....

//        for(RoomEntity room :hotel.getRooms()){
//            inventoryService.deleteAllInventory(room);
//            roomService.deleteRoomById(room.getId());
//
//        }

        hotelRepo.deleteById(id);

        return null;
    }

    @Override
    @Transactional
    public HotelDTO activateHotelById(Long id) {
        log.info("Activating the hotel with  id : {}",id);
        HotelEntity hotel = hotelRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Hotel with id : "+id+ " is not found"));
        hotel.setIsActive(true);
        hotelRepo.save(hotel);

        log.info("Rooms count: {}", hotel.getRooms().size());
        // remember to add the @Transactional when you call two db service at a tym
        for(RoomEntity room :hotel.getRooms()){
            inventoryService.initializeRoomForAYear(room);
        }

        return mapper.map(hotel,HotelDTO.class);
    }

    @Override
    @Transactional
    public HotelInfo getHotelInfoById(Long id) {
        log.info("Getting hotel info having id : {}",id);
        HotelEntity hotel = hotelRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Hotel with id : "+id+ " is not found"));
        List<RoomEntity> rooms = hotel.getRooms();
        if(rooms.isEmpty()){
            throw new ResourceNotFoundException("There is no  room in this hotel ");
        }
        List<RoomDTO> roomDTOs = rooms.stream()
                .map(room -> mapper.map(room, RoomDTO.class))
                .toList();
        return new HotelInfo(mapper.map(hotel,HotelDTO.class),roomDTOs);


    }
}
