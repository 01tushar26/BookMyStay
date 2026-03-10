package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Controller;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.LoginDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.LoginResponseDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.SignUpRequestDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.UserDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Security.AuthService;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services.RateLimiter.LoginRateLimiterService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final LoginRateLimiterService loginRateLimiterService;

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO){
        UserDTO userDTO = authService.signupUser(signUpRequestDTO);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> logIn(@RequestBody LoginDTO loginDTO, HttpServletResponse httpServletResponse ,HttpServletRequest httpServletRequest){
        String ip = httpServletRequest.getRemoteAddr();
        if(!loginRateLimiterService.allowRequest(ip)){
            return ResponseEntity
                    .status(429)
                    .body(new LoginResponseDTO("Too many login attempts. Try later."));
        }

        String[] arr= authService.loginUser(loginDTO);
        Cookie cookie = new Cookie("refreshToken",arr[1]);
        httpServletResponse.addCookie(cookie);
        LoginResponseDTO loginResponseDTO =new LoginResponseDTO(arr[0]);
        return ResponseEntity.ok(loginResponseDTO);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refreshToken(HttpServletRequest request){
        String  refreshToken  = Arrays.stream(request.getCookies())
                .filter(all ->"refreshToken".equals(all.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(()->new AuthenticationServiceException("Refresh Token is not found Login again"));
        String accessToken = authService.refresh(refreshToken);
        return ResponseEntity.ok(new LoginResponseDTO(accessToken));
    }

}
