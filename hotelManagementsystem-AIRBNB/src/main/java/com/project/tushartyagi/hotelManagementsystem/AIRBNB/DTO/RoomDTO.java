package com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class RoomDTO {

    private Long id;

    //basically we set the fetch type of Hotel is Lazy in RoomEntity

//    @JsonIgnore
//    private HotelEntity hotel;

    private String type;
    private String[] photos;
    private String[] amenities;
    private BigDecimal basePrice;
    private Integer totalCount;
    private Integer capacity;
}
