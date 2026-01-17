package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Clients;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class HolidayStartupLoader {
    private final HolidayResponse holidayResponse;

    @PostConstruct
    public void loadHolidays(){
        holidayResponse.loadHolidays(LocalDate.now().getYear());
    }
}
