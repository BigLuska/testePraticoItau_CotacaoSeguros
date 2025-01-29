package br.com.seguros.cotacao.infrastructure.mock.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("all") //retirado apontamento de sonar e testes por se tratar de um mock
public class ApoliceListenerServiceMock {
    private final MessageSenderMock messageSender;

    public ApoliceListenerServiceMock(MessageSenderMock messageSender) {
        this.messageSender = messageSender;
    }

    @RabbitListener(queues = "QueueCotacao")
    public void receiveMessage(String message) {
        System.out.println("Mensagem recebida: " + message);

        String[] parts = message.split(":");
        String idCotacao = parts[2].trim();
        String idApolice = idCotacao + "987654321";
        String response = "id_cotacao: " + idCotacao + " id_apolice: " + idApolice;

        System.out.println("Mensagem formatada: " + response);
        messageSender.sendMessage(response);
    }
}
