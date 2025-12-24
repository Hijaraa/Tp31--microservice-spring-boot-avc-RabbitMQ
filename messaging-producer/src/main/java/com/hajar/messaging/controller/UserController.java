package com.hajar.messaging.controller;

import com.hajar.messaging.model.User;
import com.hajar.messaging.service.RabbitMQProducerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final RabbitMQProducerService producerService;

    public UserController(RabbitMQProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishUser(@RequestBody User user) {
        try {
            producerService.sendMessage(user);
            return ResponseEntity.ok("User message sent to RabbitMQ successfully!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error sending message: " + e.getMessage());
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Messaging Producer service is running on port 8081");
    }
}

