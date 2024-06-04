package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/item")
@Transactional
public class ItemController {

	private final ItemRepository itemRepository;
	private final Logger logger = LogManager.getLogger(ItemController.class);

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping
	public ResponseEntity<List<Item>> getItems() {
		logger.info("Fetching all items");
		List<Item> listItems = itemRepository.findAll();
		return ResponseEntity.ok(listItems);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long itemId) {
		logger.info("Fetching item with id = {}", itemId);
		return ResponseEntity.of(itemRepository.findById(itemId));
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String itemName) {
		logger.info("Fetching items with name = {}", itemName);
		List<Item> listItems = itemRepository.findByName(itemName);
		if (Objects.isNull(listItems) || listItems.isEmpty()) {
			logger.warn("No items found with name = {}", itemName);
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(listItems);
	}

}
