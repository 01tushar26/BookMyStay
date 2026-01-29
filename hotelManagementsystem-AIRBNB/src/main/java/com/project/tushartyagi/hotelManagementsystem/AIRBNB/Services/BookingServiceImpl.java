package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.*;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.*;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Enums.BookingStatus;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Exceptions.ResourceNotFoundException;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Exceptions.UNauthorisedException;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories.*;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Strategy.PricingService;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.param.RefundCreateParams;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

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
    private final CheckOutService checkOutService;
    private final PricingService pricingService;


    @Value("${frontend.url}")
    private String frontendUrl;


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

        inventoryRepo.initBooking(room.getId(), bookingRequest.getCheckInDate(),
                bookingRequest.getCheckOutDate(), bookingRequest.getRoomCount());

//        // this is logic of getting the dynamic price of inventory using our pricing service
//        BigDecimal priceForOneRoom = pricingService.calculateTotalPrice(inventoryList);
        //also get price directly form the inventory because u update the pricethere uisng crom job
        BigDecimal priceForOneRoom = inventoryList.stream()
                .map(Inventory::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);





        BigDecimal totalPrice = priceForOneRoom.multiply(BigDecimal.valueOf(bookingRequest.getRoomCount()));





        Booking booking =Booking.builder()
                .hotel(hotel)
                .room(room)
                .checkInDate(bookingRequest.getCheckInDate())
                .checkOutDate(bookingRequest.getCheckOutDate())
                .roomCount(bookingRequest.getRoomCount())
                .status(BookingStatus.RESERVED)
                .price(totalPrice)
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
        User user = getCurrentUser();
        if(!user.getId().equals(booking.getUser().getId())){
            throw new UNauthorisedException("this booking is not belong to current user");
        }

        if(hasBookingExpired(booking)){
            throw new IllegalStateException("Booking has been expired");
        }

        if(booking.getStatus() != BookingStatus.RESERVED){
            throw new IllegalStateException("Booking is not under reserved state , cannot add the guests");
        }

       for(GuestDTO guestDTO:guests){

           guestDTO.setUser(mapper.map(getCurrentUser(),UserDTO.class));

         Guest guestEntity = mapper.map(guestDTO,Guest.class);
         guestRepository.save(guestEntity);
         booking.getGuest().add(guestEntity);

       }
       booking.setStatus(BookingStatus.GUESTS_ADDED);
       bookRepo.save(booking);

       return mapper.map(booking,BookingDTO.class);


    }

    @Override
    @Transactional
    public String initiatePayments(Long id) {
        User user = getCurrentUser();
        Booking booking = bookRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("No booking exist with id :"+id));
       if(!user.getId().equals(booking.getUser().getId())){
           throw new UNauthorisedException("User with id "+user.getId()+" is not owned this booking with id"+id);
       }
//       if(hasBookingExpired(booking)){
//           throw new IllegalStateException("Booking has been expired");
//       }
       if(booking.getStatus() != BookingStatus.GUESTS_ADDED){
           throw new IllegalStateException("Firstly add the guest");
       }
      String url = checkOutService.getCheckoutSession(frontendUrl+"/payment/success",frontendUrl+"/payment/failure",booking);
       booking.setStatus(BookingStatus.PAYMENTS_PENDING);
       bookRepo.save(booking);


        return url;
    }

    @Override
    @Transactional
    public void capturePayment(Event event) {

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
            if (session == null) return;
            String sessionId = session.getId();


            Booking booking =
                    bookRepo.findByPaymentSessionId(sessionId).orElseThrow(() ->
                            new ResourceNotFoundException("Booking not found for session ID: "+sessionId));
            if (booking.getStatus() == BookingStatus.CONFIRMED) {
                log.info("Booking already confirmed");
                return;
            }
            booking.setStatus(BookingStatus.CONFIRMED);
            bookRepo.save(booking);

            inventoryRepo.findAndLockReservedInventory(booking.getRoom().getId(), booking.getCheckInDate(),
                    booking.getCheckOutDate(), booking.getRoomCount());

            inventoryRepo.confirmBooking(booking.getRoom().getId(), booking.getCheckInDate(),
                    booking.getCheckOutDate(), booking.getRoomCount());

            log.info("Successfully confirmed the booking for Booking ID: {}", booking.getId());
        } else {
            log.warn("Unhandled event type: {}", event.getType());
        }
    }

    @Override
    @Transactional
    public void cancelBooking(Long id) {
        User user = getCurrentUser();
        Booking booking = bookRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("No booking exist with id :"+id));
        // u compare the id not the user directly becuause u did not write the equal overrides method in user entitywithout that user from db is diff from the user from context holder
        if(!user.getId().equals(booking.getUser().getId())){
            throw new UNauthorisedException("User with id "+user.getId()+" is not owned this booking with id"+id);
        }
        if(booking.getStatus() != BookingStatus.CONFIRMED){
            throw new  IllegalStateException("Only confirmed Booking get cancelled");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        bookRepo.save(booking);

        inventoryRepo.findAndLockReservedInventory(booking.getRoom().getId(), booking.getCheckInDate(),
                booking.getCheckOutDate(), booking.getRoomCount());

        inventoryRepo.cancelBooking(booking.getRoom().getId(), booking.getCheckInDate(),
                booking.getCheckOutDate(), booking.getRoomCount());

        //Refund Logic

        try {
            Session session = Session.retrieve(booking.getPaymentSessionId());
            RefundCreateParams refundParams = RefundCreateParams.builder()
                    .setPaymentIntent(session.getPaymentIntent())
                    .build();
            // this will automaticatically refund the booking
            Refund.create(refundParams);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean hasBookingExpired(Booking booking){
        log.info("Checking whether booking is expired or not");
        return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
    }

    public User  getCurrentUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
