package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/cart")
@Transactional
public class CartController {

	private final UserRepository userRepository;
	private final CartRepository cartRepository;
	private final ItemRepository itemRepository;
	private final Logger logger = LogManager.getLogger(CartController.class);

	public CartController(UserRepository userRepository, CartRepository cartRepository, ItemRepository itemRepository) {
		this.userRepository = userRepository;
		this.cartRepository = cartRepository;
		this.itemRepository = itemRepository;
	}

	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addTocart(@RequestBody ModifyCartRequest request) {
		logger.info("Add to cart request: {}", request);
		Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
		if (!userOpt.isPresent()) {
			logger.warn("Username {} does not exist", request.getUsername());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		User user = userOpt.get();

		Optional<Item> itemOpt = itemRepository.findById(request.getItemId());
		if (!itemOpt.isPresent()) {
			logger.warn("Item ID {} does not exist", request.getItemId());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Item item = itemOpt.get();

		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
				.forEach(i -> cart.addItem(item));
		cartRepository.save(cart);
		logger.info("Items added to cart successfully for user {}", request.getUsername());
		return ResponseEntity.ok(cart);
	}

	@PostMapping("/removeFromCart")
	public ResponseEntity<Cart> removeFromcart(@RequestBody ModifyCartRequest request) {
		logger.info("Remove from cart request: {}", request);
		Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
		if (!userOpt.isPresent()) {
			logger.warn("Username {} does not exist", request.getUsername());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		User user = userOpt.get();

		Optional<Item> itemOpt = itemRepository.findById(request.getItemId());
		if (!itemOpt.isPresent()) {
			logger.warn("Item ID {} does not exist", request.getItemId());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Item item = itemOpt.get();

		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
				.forEach(i -> cart.removeItem(item));
		cartRepository.save(cart);
		logger.info("Items removed from cart successfully for user {}", request.getUsername());
		return ResponseEntity.ok(cart);
	}
}
