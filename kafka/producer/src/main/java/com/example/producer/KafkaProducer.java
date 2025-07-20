package com.example.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, RiderLocation> kafkaTemplate;

    @PostMapping("/sent")
    public String sendMessage(@RequestParam("message") String message) {
        RiderLocation riderLocation = new RiderLocation("rider123", 28.61, 77.23);

        kafkaTemplate.send("topic", riderLocation);
        return "Message sent" + message;
    }
}
