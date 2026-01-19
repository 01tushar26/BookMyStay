package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    User getUserById(Long id);

}
