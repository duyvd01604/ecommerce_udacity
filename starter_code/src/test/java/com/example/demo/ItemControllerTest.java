package com.example.demo;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ItemControllerTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemController itemController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getItems_Success() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item1");
        item1.setPrice(BigDecimal.valueOf(10.00));

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Item2");
        item2.setPrice(BigDecimal.valueOf(20.00));

        List<Item> itemList = Arrays.asList(item1, item2);

        when(itemRepository.findAll()).thenReturn(itemList);

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals(itemList, response.getBody());
    }

    @Test
    void getItemById_Success() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Item1");
        item.setPrice(BigDecimal.valueOf(10.00));

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(item, response.getBody());
    }

    @Test
    void getItemById_NotFound() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getItemsByName_Success() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item1");
        item1.setPrice(BigDecimal.valueOf(10.00));

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Item1");
        item2.setPrice(BigDecimal.valueOf(20.00));

        List<Item> itemList = Arrays.asList(item1, item2);

        when(itemRepository.findByName("Item1")).thenReturn(itemList);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("Item1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals(itemList, response.getBody());
    }

    @Test
    void getItemsByName_NotFound() {
        when(itemRepository.findByName("Item1")).thenReturn(Collections.emptyList());

        ResponseEntity<List<Item>> response = itemController.getItemsByName("Item1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
