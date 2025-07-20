package com.example.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

@Component
public class KafkaConsumer {
    @KafkaListener(topics = "topic", groupId="my-group")
    public void listen1(String message) {
        System.out.println("Rcvd msg for consumer1:"+message);
    }
    @KafkaListener(topics = "topic", groupId="my-group2")
    public void listen2(String message) {
        System.out.println("Rcvd msg for consumer2:"+message);
    }
}
