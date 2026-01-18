package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.HotelEntity;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.HotelMinPrice;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Inventory;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories.HotelMinPriceRepository;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories.HotelRepositories;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories.InventoryRepositories;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Strategy.PricingService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PricingUpdateService {

    //schedular to update the HotelMinPrice and inventory every hours

    private final HotelRepositories hotelRepo;
    private final InventoryRepositories inventoryRepo;
    private final HotelMinPriceRepository minPriceRepo;
    private final PricingService pricingService;





    // this cron is for one hr
    @Scheduled(cron = "0 0 * * * *")
    public void updatePrice(){
        int page =0;
        int batchSize =100;

        while(true){
            Page<HotelEntity> hotels = hotelRepo.findAll(PageRequest.of(page,batchSize));
            if(hotels.isEmpty()){
                break;
            }
            hotels.getContent().forEach(this::updatePrice);
            page++;

        }


    }

    private void updatePrice(HotelEntity hotelEntity) {
        log.info("updating the price of a hotel with id : {}",hotelEntity.getId());
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusYears(1);

        List<Inventory> inventoryList = inventoryRepo.findByHotelAndDateBetween(hotelEntity,startDate,endDate);
        updateInventoryPrice(inventoryList);
        updateHotelMinPrice(hotelEntity,inventoryList,startDate,endDate);
    }

    private void updateHotelMinPrice(HotelEntity hotelEntity, List<Inventory> inventoryList, LocalDate startDate, LocalDate endDate) {
        // Compute minimum price per day for the hotel
        Map<LocalDate, BigDecimal> dailyMinPrices = inventoryList.stream()
                .collect(Collectors.groupingBy(
                        Inventory::getDate,
                        Collectors.mapping(Inventory::getPrice, Collectors.minBy(Comparator.naturalOrder()))
                ))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().orElse(BigDecimal.ZERO)));

        // Prepare HotelPrice entities in bulk
        List<HotelMinPrice> hotelPrices = new ArrayList<>();
        dailyMinPrices.forEach((date, price) -> {
            HotelMinPrice hotelPrice = minPriceRepo.findByHotelAndDate(hotelEntity, date)
                    .orElse(new HotelMinPrice(hotelEntity, date));
            hotelPrice.setPrice(price);
            hotelPrices.add(hotelPrice);
        });

        // Save all HotelPrice entities in bulk
        minPriceRepo.saveAll(hotelPrices);

    }

    private void updateInventoryPrice(List<Inventory> inventoryList) {
       inventoryList.forEach(inventory -> {
           BigDecimal dynamicPrice = pricingService.calculateDynamicPrice(inventory);
           inventory.setPrice(dynamicPrice);
               }

       );

       inventoryRepo.saveAll(inventoryList);

    }

}
