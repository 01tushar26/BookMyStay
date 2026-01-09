package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services;


import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.HotelDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.HotelEntity;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Exceptions.ResourceNotFoundException;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories.HotelRepositories;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepositories hotelRepo;
    private final ModelMapper mapper;

    @Override
    public HotelDTO createNewHotel(HotelDTO hotelDTO) {
        log.info("Creating a new hotel with name : {}", hotelDTO.getName());
        HotelEntity tobeCreatedHotel = mapper.map(hotelDTO,HotelEntity.class);
        tobeCreatedHotel.setIsActive(false);
       HotelEntity newHotel = hotelRepo.save(tobeCreatedHotel);
        log.info("New Hotel is Created with id : {}", newHotel.getId());

        return mapper.map(newHotel,HotelDTO.class);
    }

    @Override
    public HotelDTO getHotelById(Long id) {

        log.info("Getting hotel with id :{}",id);

        HotelEntity hotel = hotelRepo.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Hotel with id :"+ id +"is not found"));

        return mapper.map(hotel,HotelDTO.class);

    }
}
