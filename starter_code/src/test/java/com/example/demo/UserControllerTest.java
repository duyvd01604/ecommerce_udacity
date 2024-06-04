package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserController userController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @BeforeEach
    void setUp() {
        userController = new UserController(bCryptPasswordEncoder, userRepository, cartRepository);
    }

    @Test
    void createUserSuccess() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testUser");
        request.setPassword("strongPassword123");
        request.setConfirmPassword("strongPassword123");

        when(bCryptPasswordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        // Act
        ResponseEntity<User> response = userController.createUser(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("testUser", response.getBody().getUsername());
        assertEquals("encodedPassword", response.getBody().getPassword());
    }

    @Test
    void createUserFailInvalidPassword() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testUser");
        request.setPassword("weak");
        request.setConfirmPassword("weak");

        // Act
        ResponseEntity<User> response = userController.createUser(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void findUserByIdSuccess() {
        // Arrange
        long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<User> response = userController.findById(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void findUserByIdNotFound() {
        // Arrange
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<User> response = userController.findById(userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void findByUsernameSuccess() {
        // Arrange
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<User> response = userController.findByUserName(username);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void findByUsernameNotFound() {
        // Arrange
        String username = "testUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<User> response = userController.findByUserName(username);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
