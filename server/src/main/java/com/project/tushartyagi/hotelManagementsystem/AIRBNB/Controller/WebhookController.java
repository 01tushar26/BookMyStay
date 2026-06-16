package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Controller;


import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services.BookingService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;

@Tag(
        name = "Webhook APIs",
        description = "Webhook endpoints for handling external payment gateway events and asynchronous booking payment updates."
)
@RestController
@RequestMapping(path ="/webhooks")
@RequiredArgsConstructor

public class WebhookController {

    private final BookingService bookingService;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @PostMapping("/payment")
    public ResponseEntity<Void> capturePayments(@RequestBody String payload, @RequestHeader("Stripe-Signature")String sigHeader){
     try{
         Event event = Webhook.constructEvent(payload,sigHeader,endpointSecret);
         bookingService.capturePayment(event);
       return ResponseEntity.noContent().build();
     } catch (SignatureVerificationException e) {
         throw new RuntimeException(e);
     }


    }

}
