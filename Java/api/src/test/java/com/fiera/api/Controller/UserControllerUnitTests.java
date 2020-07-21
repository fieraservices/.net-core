package com.fiera.api.Controller;

import java.util.Arrays;
import java.util.List;

import com.fiera.api.controllers.UserController;
import com.fiera.api.dtos.UserDTO;
import com.fiera.api.exceptions.CustomBadRequestException;
import com.fiera.api.exceptions.CustomNotFoundException;
import com.fiera.api.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase
public class UserControllerUnitTests {

    @Mock
    private UserService userService;

    private UserController userController;

    private UserDTO userDTO;

    private Long userId;

    @BeforeEach
    public void setup() {
        userController = new UserController(userService);
        userDTO = new UserDTO();
        userDTO.setUserId(1L);
        userDTO.setDocNumber("123456789");
        userDTO.setFirstName("Luke");
        userDTO.setLastName("Skywalker");
        userDTO.setEmail("jedi@email.com");
        userDTO.setPhone("987654321");
        userDTO.setAddress("123 death star avenue");
        userId = userDTO.getUserId();
    }

    @Test
    void shouldReturnAllUsers() {

        List<UserDTO> usersDTO = Arrays.asList(userDTO);

        given(userService.getUsers()).willReturn(usersDTO);

        ResponseEntity<List<UserDTO>> actual = userController.getUsers();

        then(actual).as("Check if we have a response").isNotNull();
        then(actual.getStatusCodeValue()).as("Check if we have the expected status").isEqualTo(200);
        then(actual.getBody()).as("Check if we have a result").isNotNull();
        then(actual.getBody()).as("Check if we have the expected response").isEqualTo(usersDTO);
    }

    @Test
    void shouldReturnUserById() throws CustomNotFoundException {

        given(userService.getUserById(userId)).willReturn(userDTO);

        final ResponseEntity<UserDTO> actual = userController.getUserById(userId);

        then(actual).as("Check if we have a response").isNotNull();
        then(actual.getStatusCodeValue()).as("Check if we have the expected status").isEqualTo(200);
        then(actual.getBody()).as("Check if we have a result").isNotNull();
        then(actual.getBody()).as("Check if we have the expected response").isEqualTo(userDTO);
    }

    @Test
    void badIdShouldReturnNotFound() throws CustomNotFoundException {
    
        given(userService.getUserById(userId)).willThrow(new CustomNotFoundException("User not found for this id :: " + userId));

        final Throwable throwable = catchThrowable(() -> userController.getUserById(userId));

        then(throwable).as("An exception should be thrown if a bad ID is passed")
            .isInstanceOf(CustomNotFoundException.class)
            .as("Check the exception message")
            .hasMessageContaining("User not found for this id :: ");
    }

    @Test
    void shouldSaveUserSuccessFully() throws CustomBadRequestException {

        given(userService.addUser(any(UserDTO.class))).willReturn(userDTO.getUserId());

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/users");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        userDTO.setUserId(null);

        final ResponseEntity<Object> actual = userController.addUser(userDTO);
    
        then(actual).as("Check if we have a response").isNotNull();
        then(actual.getStatusCodeValue()).as("Check if we have the expected status").isEqualTo(201);
        then(actual.getHeaders().get("location").get(0)).as("Check if we have the expected location at the header").isEqualTo("http://localhost/users/1");
    }

    @Test
    void badRequestShouldReturnException() throws CustomBadRequestException {

        given(userService.addUser(userDTO)).willThrow(new CustomBadRequestException("User must have no Id"));

        final Throwable throwable = catchThrowable(() -> userController.addUser(userDTO));

        then(throwable).as("An exception should be thrown if a bad request is passed")
            .isInstanceOf(CustomBadRequestException.class)
            .as("Check the exception message")
            .hasMessageContaining("User must have no Id");
    }

    @Test
    void shouldUpdateUserSuccessFully() throws CustomBadRequestException, CustomNotFoundException {

        given(userService.updateUser(userId,userDTO)).willReturn(true);

        final ResponseEntity<Object> actual = userController.updateUser(userId,userDTO);

        then(actual).as("Check if we have a response").isNotNull();
        then(actual.getStatusCodeValue()).as("Check if we have the expected status").isEqualTo(200);
    }

    @Test
    void badIdsShouldReturnException() throws CustomBadRequestException, CustomNotFoundException {

        given(userService.updateUser(2L,userDTO)).willThrow(new CustomBadRequestException("Not matching Ids"));

        final Throwable throwable = catchThrowable(() -> userController.updateUser(2L,userDTO));

        then(throwable).as("An exception should be thrown if a bad request is passed")
            .isInstanceOf(CustomBadRequestException.class)
            .as("Check the exception message")
            .hasMessageContaining("Not matching Ids");
    }

    @Test
    void nonExistingIdShouldReturnException() throws CustomBadRequestException, CustomNotFoundException {

        given(userService.updateUser(userId,userDTO)).willThrow(new CustomNotFoundException("User not found for this id :: " + userId));

        final Throwable throwable = catchThrowable(() -> userController.updateUser(userId,userDTO));

        then(throwable).as("An exception should be thrown if a bad ID is passed")
            .isInstanceOf(CustomNotFoundException.class)
            .as("Check the exception message")
            .hasMessageContaining("User not found for this id :: ");
    }

    @Test
    void shouldDeleteUserSuccessFully() throws CustomNotFoundException {

        given(userService.deleteUser(userId)).willReturn(true);

        final ResponseEntity<Object> actual = userController.deleteUser(userId);

        then(actual).as("Check if we have a response").isNotNull();
        then(actual.getStatusCodeValue()).as("Check if we have the expected status").isEqualTo(200);
    }

    @Test
    void nonExistingIdToDeleteShouldReturnException() throws CustomNotFoundException {

        given(userService.deleteUser(userId)).willThrow(new CustomNotFoundException("User not found for this id :: " + userId));

        final Throwable throwable = catchThrowable(() -> userController.deleteUser(userId));

        then(throwable).as("An exception should be thrown if a bad ID is passed")
            .isInstanceOf(CustomNotFoundException.class)
            .as("Check the exception message")
            .hasMessageContaining("User not found for this id :: ");
    }
}