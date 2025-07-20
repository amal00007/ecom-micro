package com.ecommerce.order.service;


import com.ecommerce.order.dto.CartItemResponse;
import com.ecommerce.order.dto.OrderCreatedEvent;
import com.ecommerce.order.dto.OrderItemDTO;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.model.OrderStatus;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;

    private final RabbitTemplate rabbitTemplate;
    //private final UserRepository userRepository;

    public Optional<OrderResponse> createOrder(String userId) {
//validate for cart item
        List<CartItemResponse> cartItemList = cartService.getAllCartItems(userId);
        if (cartItemList.isEmpty()) {
            return Optional.empty();
        }
        //clear cart
        //validate for user
//        Optional<User> userOptional = userRepository.findById(userId);
//        if (userOptional.isEmpty()) {
//            return Optional.empty();
//        }
//        User user = userOptional.get();
        //calculate total price
        BigDecimal totalPrice = cartItemList.stream().
                map(CartItemResponse::getPrice).
                reduce(BigDecimal.ZERO, BigDecimal::add);

        //create order
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems = cartItemList.stream().map(item -> new OrderItem(
                null, item.getProductId(), item.getQuantity(), item.getPrice(), order
        )).toList();
        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(userId);

        OrderCreatedEvent orderCreatedEvent=new OrderCreatedEvent(savedOrder.getId(),
                                                              savedOrder.getUserId(),
                                                              savedOrder.getOrderStatus(),
                                                              savedOrder.getTotalAmount(),
                                                              mapToOrderItemDTO(savedOrder.getItems()),
                                                              savedOrder.getCreatedAt()   );
        rabbitTemplate.convertAndSend("order.exchange", "order.tracking", orderCreatedEvent);
        return Optional.of(mapToOrderResponse(savedOrder));
    }
private List<OrderItemDTO> mapToOrderItemDTO(List<OrderItem> orderItems) {
        return orderItems.stream().map(i->new OrderItemDTO(i.getId(),
                i.getProductId(),i.getQuantity(),i.getPrice(),
                i.getPrice().multiply(new BigDecimal(i.getQuantity())))).toList();
}
    private OrderResponse mapToOrderResponse(Order savedOrder) {

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getTotalAmount(),
                savedOrder.getOrderStatus(),
                savedOrder.getItems().stream().
                        map(item -> new OrderItemDTO(
                                item.getId(),
                                item.getProductId(),
                                item.getQuantity(),
                                item.getPrice(),
                                item.getPrice().multiply(new BigDecimal(item.getQuantity()))

                        )).toList(), savedOrder.getCreatedAt());
    }
}
