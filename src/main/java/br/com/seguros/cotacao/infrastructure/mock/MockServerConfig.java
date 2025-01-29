package br.com.seguros.cotacao.infrastructure.mock;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("all")
public class MockServerConfig {

    private ClientAndServer mockServer;
    private MockServerClient mockServerClient;

    @PostConstruct
    public void startMockServer() {
        mockServer = ClientAndServer.startClientAndServer(1080);
        mockServerClient = new MockServerClient("localhost", 1080);

        mockServerClient.when(
                HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/api/v1/cotacao/produto/1b2da7cc-b367-4196-8a78-9cfeec21f587")
        ).respond(
                HttpResponse.response()
                        .withStatusCode(200)
                        .withBody("{\"id\": \"1b2da7cc-b367-4196-8a78-9cfeec21f587\",\"name\": \"Seguro de Vida\",\"created_at\": \"2021-07-01T00:00:00Z\",\"active\": true,\"offers\": [\"adc56d77-348c-4bf0-908f-22d402ee715c\",\"bdc56d77-348c-4bf0-908f-22d402ee715c\",\"cdc56d77-348c-4bf0-908f-22d402ee715c\"]}")
        );

        mockServerClient.when(
                HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/api/v1/cotacao/produto/.*")
        ).respond(
                HttpResponse.response()
                        .withStatusCode(200)
                        .withBody("{\"retorno\": \"erro - Produto não encontrado\"}")
        );

        mockServerClient.when(
                HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/api/v1/cotacao/oferta/adc56d77-348c-4bf0-908f-22d402ee715c")
        ).respond(
                HttpResponse.response()
                        .withStatusCode(200)
                        .withBody("{\"id\": \"adc56d77-348c-4bf0-908f-22d402ee715c\",\"product_id\": \"1b2da7cc-b367-4196-8a78-9cfeec21f587\",\"name\": \"Seguro de Vida Familiar\",\"created_at\": \"2021-07-01T00:00:00Z\",\"active\": true,\"coverages\": {\"Incêndio\": 500000.00,\"Desastres naturais\": 600000.00,\"Responsabiliadade civil\": 80000.00,\"Roubo\": 100000.00},\"assistances\": [\"Encanador\",\"Eletricista\",\"Chaveiro 24h\",\"Assistência Funerária\"],\"monthly_premium_amount\": {\"max_amount\": 100.74,\"min_amount\": 50.00,\"suggested_amount\": 60.25}}")
        );

        mockServerClient.when(
                HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/api/v1/cotacao/oferta/.*")
        ).respond(
                HttpResponse.response()
                        .withStatusCode(200)
                        .withBody("{\"retorno\": \"erro - Oferta não encontrada\"}")
        );
    }

    @PreDestroy
    public void stopMockServer() {
        if (mockServer != null) {
            mockServer.stop();
        }
    }
}
