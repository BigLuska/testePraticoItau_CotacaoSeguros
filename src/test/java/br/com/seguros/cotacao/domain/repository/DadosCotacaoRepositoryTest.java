package br.com.seguros.cotacao.domain.repository;

import br.com.seguros.cotacao.domain.model.DadosCotacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DadosCotacaoRepositoryTest {

    private DadosCotacaoRepository dadosCotacaoRepository;

    private DadosCotacao dadosCotacao;

    @BeforeEach
    void setUp() {
        dadosCotacao = new DadosCotacao();
        dadosCotacao.setInsurance_policy_id(123L);
        dadosCotacao.setProduct_id("produto01");
        dadosCotacao.setOffer_id("oferta01");
        dadosCotacao.setCategory("categoria01");
        dadosCotacao.setTotal_monthly_premium_amount(BigDecimal.valueOf(1000));
        dadosCotacao.setTotal_coverage_amount(BigDecimal.valueOf(500000));
        dadosCotacao.setCoverages(Map.of("Incêndio", BigDecimal.valueOf(250000), "Desastres naturais", BigDecimal.valueOf(500000)));
        dadosCotacao.setAssistances(List.of("Assistência 1", "Assistência 2"));

        dadosCotacaoRepository = Mockito.mock(DadosCotacaoRepository.class);

        when(dadosCotacaoRepository.save(any(DadosCotacao.class))).thenReturn(dadosCotacao);
        when(dadosCotacaoRepository.findById(dadosCotacao.getId())).thenReturn(Optional.of(dadosCotacao));
        when(dadosCotacaoRepository.findAll()).thenReturn(List.of(dadosCotacao));
    }

    @Test
    void testFindById() {
        Optional<DadosCotacao> foundCotacao = dadosCotacaoRepository.findById(dadosCotacao.getId());

        assertTrue(foundCotacao.isPresent());
        assertEquals(dadosCotacao.getProduct_id(), foundCotacao.get().getProduct_id());
    }

    @Test
    void testFindAll() {
        var cotacoes = dadosCotacaoRepository.findAll();

        assertFalse(cotacoes.isEmpty());
        assertTrue(cotacoes.contains(dadosCotacao));
    }
}
