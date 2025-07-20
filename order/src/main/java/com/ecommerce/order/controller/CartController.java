package com.ecommerce.order.controller;


import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.dto.CartItemResponse;
import com.ecommerce.order.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(@RequestHeader("X-User-ID") String userId,
                                            @RequestBody CartItemRequest request) {

        if (!cartService.addToCart(userId, request)) {
            return ResponseEntity.badRequest().body("Product out of stock");
        }
        return ResponseEntity.ok().body("Product successfully added");

    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<String> removeItemFromCart(@RequestHeader("X-User-ID") String userId,
                                                     @PathVariable String productId){
        if(cartService.removeItemFromCart(userId,productId))
            return ResponseEntity.ok().body("Item removed from cart");
        return ResponseEntity.badRequest().body("Item not found");
    }
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemResponse>> getAllCartItems(@PathVariable String userId){
        return new ResponseEntity<>(cartService.getAllCartItems(userId), HttpStatus.OK);
    }

}
