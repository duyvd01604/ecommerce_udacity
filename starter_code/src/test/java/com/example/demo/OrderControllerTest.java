package com.example.demo;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

  private OrderController orderController;
  private UserRepository userRepository;
  private OrderRepository orderRepository;

  @BeforeEach
  void setUp() {
    userRepository = mock(UserRepository.class);
    orderRepository = mock(OrderRepository.class);
    orderController = new OrderController(userRepository, orderRepository);
  }

  @Test
  void submitOrderSuccess() {
    // Arrange
    String username = "testUser";
    User user = new User();
    user.setUsername(username);
    user.setCart(new Cart());

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
    when(orderRepository.save(any(UserOrder.class))).thenReturn(new UserOrder());

    // Act
    ResponseEntity<UserOrder> response = orderController.submit(username);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    // Verify interactions
    verify(userRepository, times(1)).findByUsername(username);
    verify(orderRepository, times(1)).save(any(UserOrder.class));
  }

  @Test
  void submitOrderUserNotFound() {
    // Arrange
    String username = "notExistUser";

    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

    // Act
    ResponseEntity<UserOrder> response = orderController.submit(username);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    // Verify interactions
    verify(userRepository, times(1)).findByUsername(username);
    verify(orderRepository, never()).save(any(UserOrder.class));
  }

  @Test
  void getOrderHistorySuccess() {
    // Arrange
    String username = "testUser";
    User user = new User();
    user.setUsername(username);

    List<UserOrder> orders = new ArrayList<>();
    orders.add(new UserOrder());
    orders.add(new UserOrder());

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
    when(orderRepository.findByUser(user)).thenReturn(orders);

    // Act
    ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(username);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, response.getBody().size());

    // Verify interactions
    verify(userRepository, times(1)).findByUsername(username);
    verify(orderRepository, times(1)).findByUser(user);
  }

  @Test
  void getOrderHistoryUserNotFound() {
    // Arrange
    String username = "notExistUser";

    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

    // Act
    ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(username);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    // Verify interactions
    verify(userRepository, times(1)).findByUsername(username);
    verify(orderRepository, never()).findByUser(any(User.class));
  }
}
