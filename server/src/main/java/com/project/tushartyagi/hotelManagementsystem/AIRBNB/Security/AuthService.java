package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Security;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.LoginDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.SignUpRequestDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.UserDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Enums.Roles;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.User;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Exceptions.ResourceNotFoundException;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepo;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public UserDTO signupUser(SignUpRequestDTO signUpRequestDTO){

        User user = userRepo.findByEmail(signUpRequestDTO.getEmail()).orElse(null);

        if(user != null){
            throw new RuntimeException("User is already present with this"+ signUpRequestDTO.getEmail()+" id" );
        }
     User newUser = mapper.map(signUpRequestDTO,User.class);
        newUser.setRoles(Set.of(Roles.GUEST));
        newUser.setPassword(passwordEncoder.encode(signUpRequestDTO.getPassword()));

        userRepo.save(newUser);
        return mapper.map(newUser,UserDTO.class);

    }
    public String[] loginUser(LoginDTO loginDTO){

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),loginDTO.getPassword()));
        User user = (User) authentication.getPrincipal();

        String[] arr = new String[2];
        arr[0] = jwtService.generateAccessToken(user);
        arr[1] = jwtService.generateRefreshToken(user);
        return arr;

    }
    public String refresh(String refreshToken){
        Long id = jwtService.getUserIdFromToken(refreshToken);
        User user = userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("User with id :"+id+"is not found"));

        String accesstoken = jwtService.generateAccessToken(user);
        return accesstoken;

    }
}
