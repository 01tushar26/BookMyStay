package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.BookingDTO;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.UserDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.UserUpdateRequestDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Booking;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.User;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Exceptions.ResourceNotFoundException;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories.BookingRepository;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.project.tushartyagi.hotelManagementsystem.AIRBNB.Util.Util.getCurrentUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepo;
    private final BookingRepository bookRepo;
    private final ModelMapper mapper;


    @Override
    public User getUserById(Long id) {
        return userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("user is not found with that id : "+id));
    }

    @Override
    public UserDTO updateProfile(UserUpdateRequestDTO updateRequestDTO) {

        log.info("Updating the user details");


        User user = getCurrentUser();
        //can use the reflection
        //cant use the modelmapper because it fill the other value which is not given as null
        if(updateRequestDTO.getDateOfBirth()!=null){
            user.setDateOfBirth(updateRequestDTO.getDateOfBirth());
        }
        if(updateRequestDTO.getGender()!=null){
            user.setGender(updateRequestDTO.getGender());
        }if(updateRequestDTO.getName()!=null){
            user.setName(updateRequestDTO.getName());
        }
        user = userRepo.save(user);

        return mapper.map(user,UserDTO.class);
    }

    //can make this method in booking service
    @Override
    public List<BookingDTO> getAllMyBooking() {
        User user = getCurrentUser();
        log.info("Getting all the booking for user with id -{}",user.getId());
        List<Booking> bookingList = bookRepo.findByUser(user);


        return bookingList.stream()
                .map(all->mapper.map(all,BookingDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getMyProfile() {

        User user = getCurrentUser();
        log.info("Getting Profile for the user with id -{}",user.getId());
        return mapper.map(user,UserDTO.class);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username).orElse(null);
    }
}
