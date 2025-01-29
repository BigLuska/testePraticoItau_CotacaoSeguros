package br.com.seguros.cotacao.controller;

import br.com.seguros.cotacao.application.service.CotacaoService;
import br.com.seguros.cotacao.domain.model.DadosCotacao;
import br.com.seguros.cotacao.domain.repository.DadosCotacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CotacaoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CotacaoService cotacaoService;

    @Mock
    private DadosCotacaoRepository dadosCotacaoRepository;

    @InjectMocks
    private CotacaoController cotacaoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cotacaoController).build();
    }

    @Test
    void testReceberDadosCotacao_Success() throws Exception {
        DadosCotacao dadosCotacao = new DadosCotacao();

        when(cotacaoService.solicitaCotacao(dadosCotacao)).thenReturn(true);

        mockMvc.perform(post("/api/v1/cotacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"campo\":\"valor\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Dados de cotação recebidos com sucesso!"));

        verify(cotacaoService, times(1)).solicitaCotacao(dadosCotacao);
    }

    @Test
    void testReceberDadosCotacao_Failure() throws Exception {
        DadosCotacao dadosCotacao = new DadosCotacao();

        when(cotacaoService.solicitaCotacao(dadosCotacao)).thenReturn(false);

        mockMvc.perform(post("/api/v1/cotacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"campo\":\"valor\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erro nos dados da cotação, favor corrigir!"));

        verify(cotacaoService, times(1)).solicitaCotacao(dadosCotacao);
    }

    @Test
    void testListarCotacoes() throws Exception {
        mockMvc.perform(get("/api/v1/cotacao/listar"))
                .andExpect(status().isOk());

        verify(dadosCotacaoRepository, times(1)).findAll();
    }

    @Test
    void testListarCotacaoPorId_Success() throws Exception {
        Long id = 1L;
        DadosCotacao dadosCotacao = new DadosCotacao();

        when(dadosCotacaoRepository.findById(id)).thenReturn(Optional.of(dadosCotacao));

        mockMvc.perform(get("/api/v1/cotacao/{id}", id))
                .andExpect(status().isOk());

        verify(dadosCotacaoRepository, times(1)).findById(id);
    }

    @Test
    void testListarCotacaoPorId_NotFound() throws Exception {
        Long id = 1L;

        when(dadosCotacaoRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/cotacao/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cotação não encontrada para o ID: 1"));

        verify(dadosCotacaoRepository, times(1)).findById(id);
    }
}
