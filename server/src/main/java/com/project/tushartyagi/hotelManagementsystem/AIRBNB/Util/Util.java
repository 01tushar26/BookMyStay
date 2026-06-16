package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Util;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

public final class Util {

    public static User getCurrentUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
