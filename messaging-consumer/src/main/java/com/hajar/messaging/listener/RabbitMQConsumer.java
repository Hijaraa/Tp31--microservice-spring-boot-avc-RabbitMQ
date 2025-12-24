package com.hajar.messaging.listener;

import com.hajar.messaging.model.User;
import com.hajar.messaging.repository.UserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {

    private final UserRepository userRepository;

    public RabbitMQConsumer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consumeMessage(User user) {
        System.out.println("========================================");
        System.out.println("Message reçu depuis RabbitMQ:");
        System.out.println("ID: " + user.getId());
        System.out.println("Prénom: " + user.getFirstName());
        System.out.println("Nom: " + user.getLastName());
        System.out.println("Email: " + user.getEmail());
        
        // Persister l'utilisateur dans MySQL
        // Note: L'ID sera généré automatiquement par la base de données
        User userToSave = new User();
        userToSave.setFirstName(user.getFirstName());
        userToSave.setLastName(user.getLastName());
        userToSave.setEmail(user.getEmail());
        
        User savedUser = userRepository.save(userToSave);
        System.out.println("Utilisateur sauvegardé dans MySQL avec l'ID: " + savedUser.getId());
        System.out.println("========================================");
    }
}

