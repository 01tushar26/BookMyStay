package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Clients;

import java.time.LocalDate;

public interface HolidayResponse {
     void loadHolidays(int year);
    LocalDate parseDate(String iso);
}
