package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Booking;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.User;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories.BookingRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckOutServiceImpl implements CheckOutService{

 private final BookingRepository bookingRepository;
    @Override
    @Transactional
    public String getCheckoutSession(String successUrl, String failureUrl, Booking booking) {
        log.info("Creating a checkout sesion for booking with id {}",booking.getId());
      User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {

            CustomerCreateParams customerCreateParams = CustomerCreateParams.builder()
                    .setEmail(user.getEmail())
                    .setName(user.getName())
                    .build();
            Customer customer = Customer.create(
                    customerCreateParams
            );

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                    .setCustomer(customer.getId())
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(failureUrl)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("inr")
                                                    .setUnitAmount(booking.getPrice().multiply(BigDecimal.valueOf(100)).longValue())
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName(booking.getHotel().getName()+":"+booking.getRoom().getType())
                                                                    .setDescription("Booking id :"+booking.getId())
                                                                    .build()
                                                    )
                                                    .build()
                                    )

                                    .build()
                    )

                    .build();

            Session session = Session.create(params);
             booking.setPaymentSessionId(session.getId());
             bookingRepository.save(booking);
            log.info("Session Created successfully for booking with id {}",booking.getId());
             return session.getUrl();

        } catch (StripeException e) {
            throw new RuntimeException(e);
        }



    }
}
