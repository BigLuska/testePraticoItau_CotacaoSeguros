package br.com.seguros.cotacao.application.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class ApoliceListener {

    private static final Logger logger = Logger.getLogger(ApoliceListener.class.getName());

    @Autowired
    public CotacaoService cotacaoService;

    @RabbitListener(queues = "QueueApolice")
    public void receiveMessage(String message) {
        logger.info("Recebendo mensagem da fila QueueApolice.");

        try {
            String[] numbers = message.replaceAll("[^0-9 ]", "").trim().split("\\s+");

            Long idCotacao = Long.valueOf(numbers[0]);
            Long idApolice = Long.valueOf(numbers[1]);

            logger.info(String.format("Processando cotação: %d, apólice: %d", idCotacao, idApolice));

            cotacaoService.gravaApoliceCotacao(idCotacao, idApolice);

            logger.info("Mensagem processada com sucesso.");
        } catch (Exception ex) {
            logger.severe("Erro ao processar mensagem: " + ex.getMessage());
        }
    }
}
