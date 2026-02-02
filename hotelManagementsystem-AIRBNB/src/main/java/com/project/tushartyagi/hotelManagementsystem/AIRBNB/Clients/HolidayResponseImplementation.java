package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Clients;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.ApiResponseClient;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.HolidayItem;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Holidays;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories.HolidaysRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class HolidayResponseImplementation  implements HolidayResponse{

    private final HolidaysRepository holidaysRepo;
    private final RestClient restClient;

   @Value("${apiKey}")
    private String apiKey;

    @Override
    public void loadHolidays(int year) {
        log.info("Try to loads the holidays in db for the year : {}",year);


        LocalDate startDate = LocalDate.of(year, 1,1);
        LocalDate endDate = LocalDate.of(year, 12,31);

        if(holidaysRepo.existsByHolidayDateBetween(startDate,endDate) == true){
            log.info("Holidays already loaded in db for a year : {}",year);
            return;
        }

        ApiResponseClient responseClient = restClient
                .get()
                .uri(uriBuilder -> uriBuilder
                .path("/holidays")
                .queryParam("api_key", apiKey)
                .queryParam("country", "IN")
                .queryParam("year", year)
                .build())
                .retrieve()
                .body(new ParameterizedTypeReference<ApiResponseClient>(){});

        List<Holidays> holidaysList = responseClient.getResponse().getHolidays()
                .stream()
                .map(all->
                        new Holidays(
                                null,
                                all.getName(),
                                parseDate(all.getDate().getIso()),
                                "IN"))
                .toList();

        holidaysRepo.saveAll(holidaysList);


        log.info("Succesfully loaded holidays for a year: {}",year);


    }

    @Override
    public LocalDate parseDate(String iso) {
         return LocalDate.parse(iso.substring(0, 10));
    }
}
