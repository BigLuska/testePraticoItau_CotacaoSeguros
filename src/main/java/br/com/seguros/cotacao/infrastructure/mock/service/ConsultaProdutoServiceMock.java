package br.com.seguros.cotacao.infrastructure.mock.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Service
@SuppressWarnings("all") //retirado apontamento de sonar e testes por se tratar de um mock
public class ConsultaProdutoServiceMock {

    @Autowired
    private final RestTemplate restTemplate;

    public ConsultaProdutoServiceMock(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    public <T> T consultaProdCatalog(String id, Class<T> responseType) {
        try {
            String url = "http://localhost:1080/api/v1/cotacao/produto/" + id;

            ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);
            byte[] responseBody = response.getBody();

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(responseBody, responseType);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar a resposta do serviço", e);
        }
    }

    public <T> T consultaOfertaCatalog(String id, Class<T> responseType) {
        try {
            String url = "http://localhost:1080/api/v1/cotacao/oferta/" + id;

            ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);
            byte[] responseBody = response.getBody();

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(responseBody, responseType);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar a resposta do serviço", e);
        }
    }
}