package com.ecommerce.order.dto;

import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {

    private Long orderId;
    private String userId;
    private OrderStatus status;
    private BigDecimal totalPrice;
    private List<OrderItemDTO> orderItems;
    private LocalDateTime createdAt;
}
