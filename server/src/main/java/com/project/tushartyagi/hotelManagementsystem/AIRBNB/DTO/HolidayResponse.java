package com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayResponse {
     private List<HolidayItem> holidays;
}
