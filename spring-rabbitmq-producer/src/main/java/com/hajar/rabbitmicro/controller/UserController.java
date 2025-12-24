package com.oussama.rabbitmicro.controller;

import com.oussama.rabbitmicro.model.User;
import com.oussama.rabbitmicro.service.RabbitMQProducerService;
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
        return ResponseEntity.ok("Producer service is running on port 8123");
    }
}

