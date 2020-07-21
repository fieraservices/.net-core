package com.fiera.api.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import com.fiera.api.dtos.UserDTO;
import com.fiera.api.exceptions.CustomBadRequestException;
import com.fiera.api.exceptions.CustomNotFoundException;
import com.fiera.api.models.User;
import com.fiera.api.repositories.UserRepository;
import com.fiera.api.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase
public class UserServiceUnitTests {

    @Mock
    private UserRepository userRepository;

    private ModelMapper modelMapper = new ModelMapper();

    private UserService userService;

    private User user;

    private UserDTO userDTO;

    private Long userId;

    @BeforeEach
    public void setup() {
        userService = new UserService(userRepository, modelMapper);
        user = new User();
        user.setUserId(1L);
        user.setDocNumber("123456789");
        user.setFirstName("Luke");
        user.setLastName("Skywalker");
        user.setEmail("jedi@email.com");
        user.setPhone("987654321");
        user.setAddress("123 death star avenue");
        userId = user.getUserId();
        userDTO = modelMapper.map(user, UserDTO.class);
    }

    @Test
    void shouldReturnAllUsers() {

        List<User> users = Arrays.asList(user);
        List<UserDTO> expected = Arrays.asList(userDTO);

        given(userRepository.findAll()).willReturn(users);

        List<UserDTO> actual = userService.getUsers();

        then(actual).as("Check if we have a result.").isNotNull();
        then(actual).as("Check if we have the same response").isEqualTo(expected);
    }

    @Test
    void shouldReturnUserById() throws CustomNotFoundException {

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        final UserDTO actual = userService.getUserById(userId);

        then(actual).as("Check if we have a result.").isNotNull();
        then(actual).as("Check if we have the same response").isEqualTo(userDTO);
    }

    @Test
    void badIdShouldReturnException() throws CustomNotFoundException {

        final Throwable throwable = catchThrowable(() -> userService.getUserById(userId));

        then(throwable).as("An exception should be thrown if a bad ID is passed")
            .isInstanceOf(CustomNotFoundException.class)
            .as("Check the exception message")
            .hasMessageContaining("User not found for this id :: ");
    }

    @Test
    void shouldSaveUserSuccessFully() throws CustomBadRequestException {
        user.setUserId(null);

        UserDTO newUserDTO = modelMapper.map(user, UserDTO.class);

        user.setUserId(randomLong());

        given(userRepository.save(any(User.class))).willReturn(user);

        Long actual = userService.addUser(newUserDTO);

        then(actual).as("Check if we have a result.").isNotNull();
        then(actual).as("Check if we have the same response").isEqualTo(user.getUserId());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void badRequestShouldReturnException() throws CustomBadRequestException {

        final Throwable throwable = catchThrowable(() -> userService.addUser(userDTO));

        then(throwable).as("An exception should be thrown if a bad request is passed")
            .isInstanceOf(CustomBadRequestException.class)
            .as("Check the exception message")
            .hasMessageContaining("User must have no Id");
    }

    @Test
    void shouldUpdateUserSuccessFully() throws CustomBadRequestException, CustomNotFoundException {

        given(userRepository.existsById(userId)).willReturn(true);
        given(userRepository.save(any(User.class))).willReturn(user);

        Boolean actual = userService.updateUser(userId,userDTO);

        then(actual).as("Check if we have a result.").isNotNull();
        then(actual).as("Check if we have the expected response").isTrue();

        verify(userRepository).save(any(User.class));
    }

    @Test
    void badIdsShouldReturnException() throws CustomBadRequestException, CustomNotFoundException {

        final Throwable throwable = catchThrowable(() -> userService.updateUser(2L,userDTO));

        then(throwable).as("An exception should be thrown if a bad request is passed")
            .isInstanceOf(CustomBadRequestException.class)
            .as("Check the exception message")
            .hasMessageContaining("Not matching Ids");
    }

    @Test
    void nonExistingIdShouldReturnException() throws CustomBadRequestException, CustomNotFoundException {

        given(userRepository.existsById(userId)).willReturn(false);

        final Throwable throwable = catchThrowable(() -> userService.updateUser(userId,userDTO));

        then(throwable).as("An exception should be thrown if a bad ID is passed")
            .isInstanceOf(CustomNotFoundException.class)
            .as("Check the exception message")
            .hasMessageContaining("User not found for this id :: ");
    }

    @Test
    void shouldDeleteUserSuccessFully() throws CustomNotFoundException {

        given(userRepository.existsById(userId)).willReturn(true);

        Boolean actual = userService.deleteUser(userId);

        then(actual).as("Check if we have a result.").isNotNull();
        then(actual).as("Check if we have the expected response").isTrue();

        verify(userRepository).deleteById(userId);
    }

    @Test
    void nonExistingIdToDeleteShouldReturnException() throws CustomNotFoundException {

        given(userRepository.existsById(userId)).willReturn(false);

        final Throwable throwable = catchThrowable(() -> userService.deleteUser(userId));

        then(throwable).as("An exception should be thrown if a bad ID is passed")
            .isInstanceOf(CustomNotFoundException.class)
            .as("Check the exception message")
            .hasMessageContaining("User not found for this id :: ");
    }

    private long randomLong() {
        return ThreadLocalRandom.current().nextLong(1000L);
    }

}