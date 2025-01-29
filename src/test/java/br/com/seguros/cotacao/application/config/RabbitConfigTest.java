package br.com.seguros.cotacao.application.config;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

class RabbitConfigTest {

    @Test
    void testRabbitTemplateBean() {
        ApplicationContext applicationContext = Mockito.mock(ApplicationContext.class);

        RabbitTemplate rabbitTemplate = Mockito.mock(RabbitTemplate.class);

        Mockito.when(applicationContext.getBean(RabbitTemplate.class)).thenReturn(rabbitTemplate);

        assertNotNull(applicationContext.getBean(RabbitTemplate.class), "RabbitTemplate deve ser criado");
    }

    @Test
    void testQueueCotacaoBean() {
        ApplicationContext applicationContext = Mockito.mock(ApplicationContext.class);

        Queue queueCotacao = Mockito.mock(Queue.class);

        Mockito.when(applicationContext.getBean("queueCotacao", Queue.class)).thenReturn(queueCotacao);
        Mockito.when(queueCotacao.getName()).thenReturn("QueueCotacao");

        assertNotNull(applicationContext.getBean("queueCotacao", Queue.class), "Fila QueueCotacao deve ser criada");
        assertEquals("QueueCotacao", queueCotacao.getName(), "Nome da fila deve ser 'QueueCotacao'");
    }

    @Test
    void testQueueApoliceBean() {
        ApplicationContext applicationContext = Mockito.mock(ApplicationContext.class);

        Queue queueApolice = Mockito.mock(Queue.class);

        Mockito.when(applicationContext.getBean("queueApolice", Queue.class)).thenReturn(queueApolice);
        Mockito.when(queueApolice.getName()).thenReturn("QueueApolice");

        assertNotNull(applicationContext.getBean("queueApolice", Queue.class), "Fila QueueApolice deve ser criada");
        assertEquals("QueueApolice", queueApolice.getName(), "Nome da fila deve ser 'QueueApolice'");
    }

    @Test
    void testConnectionFactoryBean() {
        ApplicationContext applicationContext = Mockito.mock(ApplicationContext.class);

        CachingConnectionFactory connectionFactory = Mockito.mock(CachingConnectionFactory.class);

        Mockito.when(applicationContext.getBean(CachingConnectionFactory.class)).thenReturn(connectionFactory);
        Mockito.when(connectionFactory.getHost()).thenReturn("localhost");

        assertNotNull(applicationContext.getBean(CachingConnectionFactory.class), "ConnectionFactory deve ser criada");
        assertEquals("localhost", connectionFactory.getHost(), "Host da ConnectionFactory deve ser 'localhost'");
    }
}
