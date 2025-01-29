package br.com.seguros.cotacao.application.service;

import br.com.seguros.cotacao.application.service.CotacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class ApoliceListenerTest {

    @InjectMocks
    private ApoliceListener apoliceListener;

    @Mock
    private CotacaoService cotacaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testReceiveMessage() {
        String message = "Cotacao: 12345 Apolice: 67890";
        apoliceListener.receiveMessage(message);
        verify(cotacaoService).gravaApoliceCotacao(12345L, 67890L);
    }
}
