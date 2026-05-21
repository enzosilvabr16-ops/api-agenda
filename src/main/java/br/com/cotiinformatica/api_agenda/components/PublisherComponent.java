package br.com.cotiinformatica.api_agenda.components;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PublisherComponent {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    public void sendMessage(String message) {
        try {
            rabbitTemplate.convertAndSend(queue.getName(), message);
        }
        catch (Exception e) {
            System.out.println("Falha ao enviar mensagem: " + e.getMessage());
        }

    }
}
