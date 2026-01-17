package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories.HolidaysRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class HolidaysService {

    private final HolidaysRepository holidayRepo;

    public Boolean existsByHolidayDate(LocalDate date){
        log.info("checking whether is holiday on {} or not ",date);
        return holidayRepo.existsByHolidayDate(date);
    }
}
