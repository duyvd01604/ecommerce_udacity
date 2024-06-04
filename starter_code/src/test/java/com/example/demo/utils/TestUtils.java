//package com.example.demo.utils;
//
//import com.example.demo.controllers.CartController;
//import com.example.demo.controllers.OrderController;
//import com.example.demo.controllers.UserController;
//import com.example.demo.model.requests.CreateUserRequest;
//import com.example.demo.model.requests.ModifyCartRequest;
//import org.springframework.http.ResponseEntity;
//
//public class TestUtils {
//
//    private static UserController userController;
//
//    private static CartController cartController;
//
//    private static OrderController orderController;
//
//    public static ResponseEntity<?> createUser(String name, String password, String confirmPassword) {
//        return userController.createUser(createUserRequest(name, password, confirmPassword));
//    }
//
//    public static ResponseEntity<?> createCart(String name, Long itemId, Integer quantity) {
//        return cartController.addTocart(createModifyCartRequest(name, itemId, quantity));
//    }
//
//    public static ResponseEntity<?> removeCart(String name, Long itemId, Integer quantity) {
//        return cartController.removeFromcart(createModifyCartRequest(name, itemId, quantity));
//    }
//
//    public static ResponseEntity<?> submit(String name) {
//        return orderController.submit(name);
//    }
//
//    public static ResponseEntity<?> getOrdersForUser(String name) {
//        return orderController.getOrdersForUser(name);
//    }
//
//    private static CreateUserRequest createUserRequest(String name, String password, String confirmPassword) {
//        CreateUserRequest createUserRequest = new CreateUserRequest();
//        createUserRequest.setUsername(name);
//        createUserRequest.setPassword(password);
//        createUserRequest.setConfirmPassword(confirmPassword);
//        return createUserRequest;
//    }
//
//    private static ModifyCartRequest createModifyCartRequest(String name, Long itemId, Integer quantity) {
//        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
//        modifyCartRequest.setUsername(name);
//        modifyCartRequest.setItemId(itemId);
//        modifyCartRequest.setQuantity(quantity);
//        return modifyCartRequest;
//    }
//}
