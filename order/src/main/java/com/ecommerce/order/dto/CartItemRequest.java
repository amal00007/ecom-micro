package com.ecommerce.order.dto;

import lombok.Data;

@Data
public class CartItemRequest {
    String productId;
    Integer quantity;

}
