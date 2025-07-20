package com.ecommerce.order.dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemResponse {

    private String productId;
    private String productName;
    private int quantity;
    private BigDecimal price;
    //private String productId;

}
