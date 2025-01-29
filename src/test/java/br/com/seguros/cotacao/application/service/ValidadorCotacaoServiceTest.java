package br.com.seguros.cotacao.application.service;

import br.com.seguros.cotacao.domain.model.DadosCotacao;
import br.com.seguros.cotacao.infrastructure.mock.model.OfertaDTO;
import br.com.seguros.cotacao.infrastructure.mock.model.PremiumAmountDTO;
import br.com.seguros.cotacao.infrastructure.mock.model.ProdutoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ValidadorCotacaoServiceTest {

    @InjectMocks
    private ValidadorCotacaoService validadorCotacaoService;

    @Mock
    private DadosCotacao dadosCotacao;

    @Mock
    private ProdutoDTO retornoProdutoMock;

    @Mock
    private OfertaDTO retornoOfertaMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidacaoRegrasNegocioErroProduto() {
        when(retornoProdutoMock.getRetorno()).thenReturn("erro");
        boolean resultado = validadorCotacaoService.validacaoRegrasNegocio(dadosCotacao, retornoProdutoMock, retornoOfertaMock);
        assertFalse(resultado);
    }

    @Test
    void testValidacaoRegrasNegocioErroOferta() {
        when(retornoProdutoMock.getRetorno()).thenReturn("");
        when(retornoOfertaMock.getRetorno()).thenReturn("erro");
        boolean resultado = validadorCotacaoService.validacaoRegrasNegocio(dadosCotacao, retornoProdutoMock, retornoOfertaMock);
        assertFalse(resultado);
    }

    @Test
    void testValidacaoRegrasNegocioInativoProduto() {
        when(retornoProdutoMock.isActive()).thenReturn(false);
        when(retornoOfertaMock.isActive()).thenReturn(true);
        PremiumAmountDTO premiumAmountMock = mock(PremiumAmountDTO.class);
        when(retornoOfertaMock.getMonthlyPremiumAmount()).thenReturn(premiumAmountMock);
        when(premiumAmountMock.getMinAmount()).thenReturn(new BigDecimal("50.00"));
        when(premiumAmountMock.getMaxAmount()).thenReturn(new BigDecimal("150.00"));
        when(dadosCotacao.getTotal_monthly_premium_amount()).thenReturn(new BigDecimal("100.00"));
        when(dadosCotacao.getTotal_coverage_amount()).thenReturn(new BigDecimal("500.00"));
        boolean resultado = validadorCotacaoService.validacaoRegrasNegocio(dadosCotacao, retornoProdutoMock, retornoOfertaMock);
        assertFalse(resultado);
    }

    @Test
    void testValidacaoCoberturaInvalida() {
        Map<String, BigDecimal> coverages = new HashMap<>();
        coverages.put("cobertura1", new BigDecimal("100"));
        when(dadosCotacao.getCoverages()).thenReturn(coverages);
        when(retornoOfertaMock.getCoverages()).thenReturn(Map.of("cobertura1", new BigDecimal("50")));
        boolean resultado = validadorCotacaoService.validarCoberturas(dadosCotacao, retornoOfertaMock);
        assertFalse(resultado);
    }

    @Test
    void testValidacaoAssistenciasInvalida() {
        List<String> assistances = Arrays.asList("assistencia1");
        when(dadosCotacao.getAssistances()).thenReturn(assistances);
        when(retornoOfertaMock.getAssistances()).thenReturn(Collections.emptyList());
        boolean resultado = validadorCotacaoService.validarAssistencias(dadosCotacao, retornoOfertaMock);
        assertFalse(resultado);
    }

    @Test
    void testValidacaoPremioMensalForaDoIntervalo() {
        when(dadosCotacao.getTotal_monthly_premium_amount()).thenReturn(new BigDecimal("150"));
        PremiumAmountDTO premiumAmountDTO = mock(PremiumAmountDTO.class);
        when(premiumAmountDTO.getMinAmount()).thenReturn(new BigDecimal("100"));
        when(premiumAmountDTO.getMaxAmount()).thenReturn(new BigDecimal("120"));
        when(retornoOfertaMock.getMonthlyPremiumAmount()).thenReturn(premiumAmountDTO);
        boolean resultado = validadorCotacaoService.validarPremioMensal(dadosCotacao, retornoOfertaMock);
        assertFalse(resultado);
    }

    @Test
    void testValidacaoTotalCoberturaValida() {
        Map<String, BigDecimal> coverages = new HashMap<>();
        coverages.put("cobertura1", new BigDecimal("50"));
        coverages.put("cobertura2", new BigDecimal("30"));
        when(dadosCotacao.getCoverages()).thenReturn(coverages);
        when(dadosCotacao.getTotal_coverage_amount()).thenReturn(new BigDecimal("80"));
        boolean resultado = validadorCotacaoService.validarTotalCoberturas(dadosCotacao);
        assertTrue(resultado);
    }

    @Test
    void testValidacaoTotalCoberturaInvalida() {
        Map<String, BigDecimal> coverages = new HashMap<>();
        coverages.put("cobertura1", new BigDecimal("50"));
        coverages.put("cobertura2", new BigDecimal("40"));
        when(dadosCotacao.getCoverages()).thenReturn(coverages);
        when(dadosCotacao.getTotal_coverage_amount()).thenReturn(new BigDecimal("100"));
        boolean resultado = validadorCotacaoService.validarTotalCoberturas(dadosCotacao);
        assertFalse(resultado);
    }
}
