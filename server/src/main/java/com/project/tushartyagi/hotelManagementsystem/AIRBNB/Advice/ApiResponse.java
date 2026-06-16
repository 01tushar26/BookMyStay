package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Advice;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ApiResponse<t> {
     private t data;
     private LocalDateTime time;
     private ApiError error;

    public ApiResponse(t data) {
        this.data = data;
        this.time =LocalDateTime.now();
    }
}
