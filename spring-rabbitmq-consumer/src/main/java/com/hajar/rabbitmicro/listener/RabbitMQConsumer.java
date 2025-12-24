package com.hajar.rabbitmicro.listener;

import com.hajar.rabbitmicro.model.User;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consumeMessage(User user) {
        System.out.println("========================================");
        System.out.println("Message reçu depuis RabbitMQ:");
        System.out.println("ID: " + user.getId());
        System.out.println("Prénom: " + user.getFirstName());
        System.out.println("Nom: " + user.getLastName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("========================================");
    }
}

