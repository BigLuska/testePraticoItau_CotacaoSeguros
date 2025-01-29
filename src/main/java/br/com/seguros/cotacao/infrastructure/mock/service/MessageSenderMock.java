package br.com.seguros.cotacao.infrastructure.mock.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("all") //retirado apontamento de sonar e testes por se tratar de um mock
public class MessageSenderMock {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageSenderMock(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend("QueueApolice", message);
        System.out.println("Sent message: " + message);
    }
}