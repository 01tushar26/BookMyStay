package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Holidays;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface HolidaysRepository extends JpaRepository<Holidays, Long> {

    Boolean existsByHolidayDate(LocalDate date);

    Boolean existsByHolidayDateBetween(LocalDate startDate,LocalDate endDate);
}