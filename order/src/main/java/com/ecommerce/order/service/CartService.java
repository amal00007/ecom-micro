package com.ecommerce.order.service;

import com.ecommerce.order.clients.ProductServiceClient;
import com.ecommerce.order.clients.UserServiceClient;
import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.dto.CartItemResponse;
import com.ecommerce.order.dto.ProductResponse;
import com.ecommerce.order.dto.UserResponse;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.repository.CartItemRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    // private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;
    int attempt = 0;

    //private final UserRepository userRepository;
    //@CircuitBreaker(name = "productService",fallbackMethod = "addToCartFallBack")
    @Retry(name = "retryService", fallbackMethod = "addToCartFallBack")
    public boolean addToCart(String userId, CartItemRequest request) {
        System.out.println(attempt);
        ProductResponse productResponse = productServiceClient.getProductDetails(Long.valueOf(request.getProductId()));
        if (productResponse == null || productResponse.getStockQuantity() < request.getQuantity()) {
            return false;
        }
        List<UserResponse> user = userServiceClient.getUser(userId);
        if (user.isEmpty())
            return false;

        CartItem existingItem = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId());

        if (existingItem != null) {
//update the quantity
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            existingItem.setPrice(BigDecimal.valueOf(100.00));
            cartItemRepository.save(existingItem);
        } else {
            CartItem cartItem = new CartItem();

            cartItem.setUserId(userId);
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(BigDecimal.valueOf(100.00));
            cartItemRepository.save(cartItem);
//create new cart item
        }
        return true;
    }

    public boolean removeItemFromCart(String userId, String productId) {

//        Optional<Product> productOpt = productRepository.findById(productId);
//        Optional<User> userOpt = userRepository.findById(userId);
//        if (productOpt.isPresent() && userOpt.isPresent()) {
//            cartItemRepository.deleteByUserIdAndProductId(userId, productId);
//            return true;
//        }
//        return false;
        cartItemRepository.deleteByUserIdAndProductId(userId, productId);
        return true;
    }

    public List<CartItemResponse> getAllCartItems(String userId) {
        return cartItemRepository.findByUserId(userId).stream().
                map(this::mapToCartResponse).
                collect(Collectors.toList());
    }

    public CartItemResponse mapToCartResponse(CartItem cart) {
        CartItemResponse response = new CartItemResponse();
        response.setQuantity(cart.getQuantity());
        response.setProductId(cart.getProductId());
        response.setProductName(cart.getProductId());
        response.setPrice(cart.getPrice());
        //response.setProduct(cart.getProductId());
        return response;
    }

    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);

    }

    public boolean addToCartFallBack(String userId, CartItemRequest request, Exception exception) {
        System.out.println("Falling back");
        return false;
    }
}
