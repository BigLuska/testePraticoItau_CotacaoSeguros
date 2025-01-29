package br.com.seguros.cotacao.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

class DadosCotacaoTest {

    @Mock
    private DadosCotacao dadosCotacao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCoverages() {
        Map<String, BigDecimal> coverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000.00),
                "Desastres naturais", BigDecimal.valueOf(500000.00)
        );

        when(dadosCotacao.getCoverages()).thenReturn(coverages);

        Map<String, BigDecimal> result = dadosCotacao.getCoverages();

        assert(result != null);
        assert(result.size() == 2);
        assert(result.containsKey("Incêndio"));
        assert(result.containsValue(BigDecimal.valueOf(250000.00)));

        verify(dadosCotacao, times(1)).getCoverages();
    }

    @Test
    void testAssistances() {
        List<String> assistances = List.of("Assistência 1", "Assistência 2");

        when(dadosCotacao.getAssistances()).thenReturn(assistances);

        List<String> result = dadosCotacao.getAssistances();

        assert(result != null);
        assert(result.size() == 2);
        assert(result.contains("Assistência 1"));
        assert(result.contains("Assistência 2"));

        verify(dadosCotacao, times(1)).getAssistances();
    }
}
