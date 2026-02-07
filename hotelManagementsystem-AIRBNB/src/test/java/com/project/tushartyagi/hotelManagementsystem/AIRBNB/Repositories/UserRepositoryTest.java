package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Enums.Gender;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ImportAutoConfiguration(exclude = SecurityAutoConfiguration.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    private User user;

    @BeforeEach
     void setUp(){
        user = new User();
        user.setName("Tushar");
        user.setEmail("Hello@gmail.com");
        user.setPassword("tushar123");
        user.setDateOfBirth(LocalDate.of(2003, 1,26));
        user.setGender(Gender.MALE);

    }


    @Test
    void TestFindByEmail_WhenEmailIsPresent_ThenReturnUser() {

        //given,Arrange
        userRepository.save(user);

        //act,when
        Optional<User> u = userRepository.findByEmail(user.getEmail());

        //then
        Assertions.assertThat(u).isNotNull();
        Assertions.assertThat(u).isNotEmpty();
        Assertions.assertThat(u.get().getEmail()).isEqualTo(user.getEmail());

    }
    @Test
    void TestFindByEmail_WhenEmailIsNotPresent_ThenReturnEmptyUser() {

        //given,Arrange
       String email = "tushartyagi2601@gamil.com";

        //act,when
        Optional<User> u = userRepository.findByEmail(email);

        //then
        Assertions.assertThat(u).isNotNull();
        Assertions.assertThat(u).isEmpty();


    }
}