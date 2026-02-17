package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Controller;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


//todo- this controller give error thats th reason it does not show in your aws dashboards
@RestController
@Profile("prod")
public class HealthCheckController {
    @GetMapping("/")
    public ResponseEntity<String> getHealthCheckAWS(){
        return ResponseEntity.ok("OK");
    }
}
