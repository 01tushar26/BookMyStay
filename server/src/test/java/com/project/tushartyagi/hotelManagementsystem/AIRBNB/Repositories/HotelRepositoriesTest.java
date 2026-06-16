package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Enums.Gender;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.HotelEntity;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.User;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.TestcontainerConfigurations;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;
import java.util.TimeZone;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainerConfigurations.class)
//this is remove the securtity authenrication part from the test
@ImportAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@EntityScan(basePackages = "com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity")
class HotelRepositoriesTest {

    @Autowired
    private HotelRepositories hotelRepo;
    @Autowired
    private UserRepository userRepo;

    private HotelEntity hotel ;
    private User user;


    @BeforeAll
    static void setTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
    }

    @BeforeEach
    void setup(){
         hotel = new HotelEntity();
         user = new User();
        user.setName("Tushar");
        user.setEmail("Hello@gmail.com");
        user.setPassword("tushar123");
        user.setDateOfBirth(LocalDate.of(2003, 1,26));
        user.setGender(Gender.MALE);

        hotel.setName("Taj Hotel");
        hotel.setOwner(user);
        hotel.setIsActive(false);
    }

    @Test
    void TestExistsByName_WhenNameIsPresent_returnTrue() {
        userRepo.save(user);
        hotelRepo.save(hotel);

        Boolean b= hotelRepo.existsByName(hotel.getName());

         assertThat(b).isTrue();

    }
    @Test
    void TestExistsByName_WhenNameIsNotPresent_returnFalse() {
        String name = "taj";

        Boolean b= hotelRepo.existsByName(name);
         assertThat(b).isNotNull();
         assertThat(b).isFalse();

    }

    @Test
    void testFindByOwner_WhenOwnerIsPresent_returnHotelEntity() {
        //arrange,Given
        userRepo.save(user);
        hotelRepo.save(hotel);
       //Act,When
        List<HotelEntity> h= hotelRepo.findByOwner(user);
       //Assert ,Then
        assertThat(h).isNotNull();
        assertThat(h.getFirst()).isEqualTo(hotel);

    }
    @Test
    void testFindByOwner_WhenOwnerIsNotPresent_returnNull() {
        userRepo.save(user);

        List<HotelEntity> h= hotelRepo.findByOwner(user);


        Assertions.assertThat(h).isNotNull().isEmpty();

    }
}