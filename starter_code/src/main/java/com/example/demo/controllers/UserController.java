package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@Transactional
public class UserController {

	private final Logger logger = LogManager.getLogger(UserController.class);
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final UserRepository userRepository;
	private final CartRepository cartRepository;

    public UserController(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, CartRepository cartRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    @GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		logger.info("Finding user by id: {}", id);
		Optional<User> optionalUser = userRepository.findById(id);
		if (!optionalUser.isPresent()) {
			logger.error("User with id {} not found", id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		logger.info("User with id {} found successfully", id);
		return ResponseEntity.ok(optionalUser.get());
	}

	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		logger.info("Finding user by username: {}", username);
		Optional<User> optionalUser = userRepository.findByUsername(username);
		if (!optionalUser.isPresent()) {
			logger.error("User with username {} not found", username);
			return ResponseEntity.notFound().build();
		}
		logger.info("User with username {} found successfully", username);
		return ResponseEntity.ok(optionalUser.get());
	}

	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		logger.info("CreateUserRequest received: {}", createUserRequest);

		if (createUserRequest.getPassword().length() <= 10) {
			logger.error("Password length must be greater than 10 characters");
			return ResponseEntity.badRequest().body(null);
		}

		if (!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
			logger.error("Password and confirm password do not match");
			return ResponseEntity.badRequest().body(null);
		}

		User user = new User();
		user.setUsername(createUserRequest.getUsername());

		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);

		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
		userRepository.save(user);

		logger.info("User created successfully with username: {}", createUserRequest.getUsername());
		return ResponseEntity.ok(user);
	}
}
