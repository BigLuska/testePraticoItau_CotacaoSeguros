package br.com.seguros.cotacao.application.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import br.com.seguros.cotacao.domain.model.DadosCotacao;
import br.com.seguros.cotacao.infrastructure.mock.MockServerConfig;
import br.com.seguros.cotacao.infrastructure.mock.model.OfertaDTO;
import br.com.seguros.cotacao.infrastructure.mock.model.ProdutoDTO;
import br.com.seguros.cotacao.infrastructure.mock.model.PremiumAmountDTO;
import br.com.seguros.cotacao.infrastructure.mock.service.ConsultaProdutoServiceMock;
import br.com.seguros.cotacao.domain.repository.DadosCotacaoRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

public class CotacaoServiceTest {

    @InjectMocks
    private CotacaoService cotacaoService;

    @Mock
    private ValidadorCotacaoService validadorCotacaoService;

    @Mock
    private ConsultaProdutoServiceMock consultaProdutoService;

    private DadosCotacao dadosCotacao;
    private ProdutoDTO produtoDTO;
    private OfertaDTO ofertaDTO;

    @Mock
    private DadosCotacaoRepository dadosCotacaoRepository;

    @Mock
    private MessageSender messageSender;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.openMocks(this);
        cotacaoService = new CotacaoService(messageSender);
        cotacaoService.dadosCotacaoRepository = dadosCotacaoRepository;

        dadosCotacao = new DadosCotacao();
        dadosCotacao.setProduct_id("1b2da7cc-b367-4196-8a78-9cfeec21f587");
        dadosCotacao.setOffer_id("adc56d77-348c-4bf0-908f-22d402ee715c");
        dadosCotacao.setCategory("HOME");
        dadosCotacao.setTotal_monthly_premium_amount(BigDecimal.valueOf(75.25));
        dadosCotacao.setTotal_coverage_amount(BigDecimal.valueOf(825000.00));
        dadosCotacao.setCoverages(Map.of(
                "Incêndio", BigDecimal.valueOf(250000.00),
                "Desastres naturais", BigDecimal.valueOf(500000.00),
                "Responsabilidade civil", BigDecimal.valueOf(75000.00)
        ));
        dadosCotacao.setAssistances(Arrays.asList("Encanador", "Eletricista", "Chaveiro 24h"));

        produtoDTO = new ProdutoDTO();
        produtoDTO.setId("1b2da7cc-b367-4196-8a78-9cfeec21f587");
        produtoDTO.setName("Seguro de Vida");
        produtoDTO.setCreatedAt("2021-07-01T00:00:00Z");
        produtoDTO.setActive(true);
        produtoDTO.setOffers(Arrays.asList("adc56d77-348c-4bf0-908f-22d402ee715c", "bdc56d77-348c-4bf0-908f-22d402ee715c", "cdc56d77-348c-4bf0-908f-22d402ee715c"));

        PremiumAmountDTO premiumAmountDTO = new PremiumAmountDTO();
        premiumAmountDTO.setMaxAmount(BigDecimal.valueOf(100.74));
        premiumAmountDTO.setMinAmount(BigDecimal.valueOf(50.00));
        premiumAmountDTO.setSuggestedAmount(BigDecimal.valueOf(60.25));

        ofertaDTO = new OfertaDTO();
        ofertaDTO.setId("adc56d77-348c-4bf0-908f-22d402ee715c");
        ofertaDTO.setProductId("1b2da7cc-b367-4196-8a78-9cfeec21f587");
        ofertaDTO.setName("Seguro de Vida Familiar");
        ofertaDTO.setCreatedAt("2021-07-01T00:00:00Z");
        ofertaDTO.setActive(true);
        ofertaDTO.setCoverages(Map.of(
                "Incêndio", BigDecimal.valueOf(500000.00),
                "Desastres naturais", BigDecimal.valueOf(600000.00),
                "Responsabilidade civil", BigDecimal.valueOf(80000.00),
                "Roubo", BigDecimal.valueOf(100000.00)
        ));
        ofertaDTO.setAssistances(Arrays.asList("Encanador", "Eletricista", "Chaveiro 24h", "Assistência Funerária"));
        ofertaDTO.setMonthlyPremiumAmount(premiumAmountDTO);
    }

    @Test
    public void testSolicitaCotacao_Valida() {
        when(consultaProdutoService.consultaProdCatalog(dadosCotacao.getProduct_id(), ProdutoDTO.class)).thenReturn(produtoDTO);
        when(consultaProdutoService.consultaOfertaCatalog(dadosCotacao.getOffer_id(), OfertaDTO.class)).thenReturn(ofertaDTO);

        doReturn(true).when(validadorCotacaoService).validacaoRegrasNegocio(dadosCotacao, produtoDTO, ofertaDTO);

        boolean cotacaoValida = validadorCotacaoService.validacaoRegrasNegocio(dadosCotacao,
                consultaProdutoService.consultaProdCatalog(dadosCotacao.getProduct_id(), ProdutoDTO.class),
                consultaProdutoService.consultaOfertaCatalog(dadosCotacao.getOffer_id(), OfertaDTO.class));

        assertTrue(cotacaoValida);
    }

    @Test
    public void testGravaApoliceCotacao() {
        Long idCotacao = 100L;
        Long idApolice = 200L;
        DadosCotacao dadosCotacao = new DadosCotacao();
        dadosCotacao.setId(idCotacao);

        when(dadosCotacaoRepository.findById(idCotacao)).thenReturn(java.util.Optional.of(dadosCotacao));
        when(dadosCotacaoRepository.save(dadosCotacao)).thenReturn(dadosCotacao);

        cotacaoService.gravaApoliceCotacao(idCotacao, idApolice);

        assertEquals(idApolice, dadosCotacao.getInsurance_policy_id());
        verify(dadosCotacaoRepository).save(dadosCotacao);
    }

    @Test
    public void testGravaApoliceCotacao_cotacaoNaoEncontrada() {
        Long idCotacao = 100L;
        Long idApolice = 200L;

        when(dadosCotacaoRepository.findById(idCotacao)).thenReturn(java.util.Optional.empty());

        assertDoesNotThrow(() -> cotacaoService.gravaApoliceCotacao(idCotacao, idApolice));
    }

    @Test
    public void testGravaApoliceCotacao_erroConversao() {
        Long idCotacao = 100L;
        Long idApolice = 200L;
        DadosCotacao dadosCotacao = new DadosCotacao();
        dadosCotacao.setId(idCotacao);

        when(dadosCotacaoRepository.findById(idCotacao)).thenReturn(java.util.Optional.of(dadosCotacao));

        cotacaoService.gravaApoliceCotacao(idCotacao, idApolice);

        assertEquals(idApolice, dadosCotacao.getInsurance_policy_id());
        verify(dadosCotacaoRepository).save(dadosCotacao);
    }
}
