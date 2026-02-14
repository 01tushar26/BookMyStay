package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.User;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Exceptions.ResourceNotFoundException;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Repositories.UserRepository;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.TestcontainerConfigurations;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Optional;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)



class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void TestGetUserById_WhenUserIsPresent_ReturnUser() {

        //assign the mock
        User mock_user = new User();
        mock_user.setId(1L);
        mock_user.setPassword("xyz");
        mock_user.setEmail("presentUser@gmail.com");
        mock_user.setName("testing_user");
        Long id = mock_user.getId();
        when(userRepository.findById(id)).thenReturn(Optional.of(mock_user));
        //act
        User user =userService.getUserById(id);
        //then
        Assertions.assertThat(user).isNotNull().isEqualTo(mock_user);
//        Checking if certain methods were called on mock objects
        verify(userRepository,atLeastOnce()).findById(id);


    }
    @Test
    void TestGetUserById_WhenUserIsNotPresent_ThrowException() {

        //assign the mock
        Long id =1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        //act  and then
        Assertions.assertThatThrownBy(()->userService.getUserById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("user is not found with that id : "+id);
        //verify
        verify(userRepository,atLeastOnce()).findById(id);


    }


}