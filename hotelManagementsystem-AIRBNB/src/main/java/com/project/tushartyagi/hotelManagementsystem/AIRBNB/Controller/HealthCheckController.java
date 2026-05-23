package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


//todo- this controller give error that's the reason it does not show in your aws dashboards
@RestController
@Profile("prod")
@Tag(name = "Aws health check endpoint")
public class HealthCheckController {
    @GetMapping("/")
    public ResponseEntity<String> getHealthCheckAWS(){
        return ResponseEntity.ok("OK");
    }
}
