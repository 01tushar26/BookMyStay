package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.*;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.*;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Enums.BookingStatus;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Exceptions.ResourceNotFoundException;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.jaxb.SourceType;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{
    private final GuestRepository guestRepository;
    private final BookingRepository bookRepo;
     private final HotelRepositories hotelRepo;
     private final RoomRepositories roomRepo;
     private final InventoryRepositories inventoryRepo;
     private final ModelMapper mapper;

    @Override
    @Transactional
    public BookingDTO initializeBooking(BookingRequest bookingRequest) {
       log.info("initializing booking for hotel with id {} and room with id{} from {} to {}",bookingRequest.getHotelId(),bookingRequest.getRoomId(),bookingRequest.getCheckInDate(),bookingRequest.getCheckOutDate());
        HotelEntity hotel = hotelRepo.findById(
                bookingRequest.getHotelId())
                .orElseThrow(
                        ()->new ResourceNotFoundException("hotel with id :"+bookingRequest.getHotelId()+"is not found !"));

        RoomEntity room = roomRepo.findByIdAndHotelId(bookingRequest.getRoomId(), bookingRequest.getHotelId())
                .orElseThrow(()->new ResourceNotFoundException("This type of room is not available in this hotel"));


        List<Inventory> inventoryList = inventoryRepo
                .findAndLockAvailableInventories(bookingRequest.getCheckInDate(),bookingRequest.getCheckOutDate(),bookingRequest.getRoomCount(), bookingRequest.getRoomId());
        long dayCount = ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(),bookingRequest.getCheckOutDate()) +1;

        if(dayCount != inventoryList.size()){
            throw new IllegalStateException("No rooms Available anymore");
        }

        for(Inventory inventory:inventoryList){
//            System.out.println(inventory.getHotel().getId());
//            System.out.println(inventory.getRoom().getId());
            inventory.setReservedCount(inventory.getReservedCount()+ bookingRequest.getRoomCount());
        }

        inventoryRepo.saveAll(inventoryList);

        // create booking



        //TODO : add dynamic pricing strategy

        Booking booking =Booking.builder()
                .hotel(hotel)
                .room(room)
                .checkInDate(bookingRequest.getCheckInDate())
                .checkOutDate(bookingRequest.getCheckOutDate())
                .roomCount(bookingRequest.getRoomCount())
                .status(BookingStatus.RESERVED)
                .price(BigDecimal.TEN)
                .user(getCurrentUser())
                .build();
        bookRepo.save(booking);

        return mapper.map(booking, BookingDTO.class);




    }

    @Override
    @Transactional
    public BookingDTO addGuest(List<GuestDTO> guests, Long id) {
        log.info("Adding guests to a booking with id {}",id);
        Booking booking = bookRepo.findById(
                        id)
                .orElseThrow(
                        ()->new ResourceNotFoundException("No booking is available with id :"+id));

        if(hasBookingExpired(booking)){
            throw new IllegalStateException("Booking has been expired");
        }

        if(booking.getStatus() != BookingStatus.RESERVED){
            throw new IllegalStateException("Booking is not under reserved state , cannot add the guests");
        }

       for(GuestDTO guestDTO:guests){

           guestDTO.setUser(getCurrentUser());

         Guest guestEntity = mapper.map(guestDTO,Guest.class);
         guestRepository.save(guestEntity);
         booking.getGuest().add(guestEntity);

       }
       booking.setStatus(BookingStatus.GUESTS_ADDED);
       bookRepo.save(booking);

       return mapper.map(booking,BookingDTO.class);


    }

    public Boolean hasBookingExpired(Booking booking){
        return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
    }

    public User  getCurrentUser(){
        //TODO : remove this dummy user
        User user = new User();

        user.setId(1L);
        return user;
    }

}
