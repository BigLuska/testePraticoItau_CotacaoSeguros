package br.com.seguros.cotacao.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.*;

class MessageSenderTest {

    @InjectMocks
    private MessageSender messageSender;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);}

    @Test
    void testSendMessage() {
        String message = "Test message";
        messageSender.sendMessage(message);
        verify(rabbitTemplate).convertAndSend("QueueCotacao", message);
    }
}
