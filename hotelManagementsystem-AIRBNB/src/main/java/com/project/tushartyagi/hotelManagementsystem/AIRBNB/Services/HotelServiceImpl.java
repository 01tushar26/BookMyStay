package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services;


import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.HotelDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.HotelEntity;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Exceptions.AlreadyExistException;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories.HotelRepositories;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepositories hotelRepo;
    private final ModelMapper mapper;

    @Override
    public HotelDTO createNewHotel(HotelDTO hotelDTO) {
        log.info("Creating a new hotel with name : {}", hotelDTO.getName());
        Boolean isExistbyname = hotelRepo.existsByName( hotelDTO.getName());
        if(isExistbyname == true){

            throw new AlreadyExistException("Hotel with this name:"+hotelDTO.getName()+" already Exist");
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
}
