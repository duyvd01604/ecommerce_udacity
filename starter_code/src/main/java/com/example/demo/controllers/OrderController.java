package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/api/order")
@Transactional
public class OrderController {

	private final UserRepository userRepository;
	private final OrderRepository orderRepository;
	private final Logger logger = LogManager.getLogger(OrderController.class);

    public OrderController(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		logger.info("Submitting order for username: {}", username);
		User user = userRepository.findByUsername(username).orElse(null);
		if (user == null || user.getCart() == null) {
			logger.warn("User or user's cart not found for username: {}", username);
			return ResponseEntity.notFound().build();
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);
		logger.info("Order submitted successfully for username: {}", username);
		return ResponseEntity.ok(order);
	}

	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		logger.info("Fetching order history for username: {}", username);
		User user = userRepository.findByUsername(username).orElse(null);
		if (user == null) {
			logger.warn("Username {} does not exist", username);
			return ResponseEntity.notFound().build();
		}
		List<UserOrder> orderList = orderRepository.findByUser(user);
		logger.info("Order history retrieved for username: {}", username);
		return ResponseEntity.ok(orderList);
	}
}
