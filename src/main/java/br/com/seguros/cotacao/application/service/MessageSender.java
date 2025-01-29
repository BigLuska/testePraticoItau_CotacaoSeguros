package br.com.seguros.cotacao.application.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class MessageSender {

    private static final Logger logger = Logger.getLogger(MessageSender.class.getName());
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String message) {
        logger.info("Iniciando o envio da mensagem para a fila.");

        try {
            rabbitTemplate.convertAndSend("QueueCotacao", message);
            logger.info(String.format("Mensagem enviada com sucesso: %s", message));
        } catch (Exception e) {
            logger.severe(String.format("Erro ao enviar mensagem: %s", e.getMessage()));
        }
    }
}
