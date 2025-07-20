package com.ecom.notification;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderEventConsumer {
    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void handleOrderEvent(OrderCreatedEvent orderCreatedEvent) {
        System.out.println("Received Order Event" + orderCreatedEvent);

        long orderId = orderCreatedEvent.getOrderId();
        String status= String.valueOf(orderCreatedEvent.getStatus());
        System.out.println("Order Id: " + orderId);
        System.out.println("Status: " + status);
    }

}
