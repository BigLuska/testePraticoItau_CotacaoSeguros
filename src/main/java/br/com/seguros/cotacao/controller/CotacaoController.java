package br.com.seguros.cotacao.controller;

import br.com.seguros.cotacao.application.service.CotacaoService;
import br.com.seguros.cotacao.domain.model.DadosCotacao;
import br.com.seguros.cotacao.domain.repository.DadosCotacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/cotacao")
public class CotacaoController {

    private static final Logger logger = Logger.getLogger(CotacaoController.class.getName());

    @Autowired
    private CotacaoService cotacaoService;

    @Autowired
    private DadosCotacaoRepository dadosCotacaoRepository;

    @PostMapping
    public ResponseEntity<String> receberDadosCotacao(@RequestBody DadosCotacao dadosCotacao) {
        logger.info("Recebendo dados de cotação: " + dadosCotacao);

        if (!cotacaoService.solicitaCotacao(dadosCotacao)){
            logger.warning("Erro nos dados da cotação: " + dadosCotacao);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro nos dados da cotação, favor corrigir!");
        } else {
            logger.info("Dados de cotação recebidos com sucesso: " + dadosCotacao);
            return ResponseEntity.status(HttpStatus.CREATED).body("Dados de cotação recebidos com sucesso!");
        }
    }

    @GetMapping("/listar")
    public List<DadosCotacao> listarCotacoes() {
        logger.info("Listando todas as cotações.");
        List<DadosCotacao> cotacoes = dadosCotacaoRepository.findAll();
        logger.info("Total de cotações encontradas: " + cotacoes.size());
        return cotacoes;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> listarCotacaoPorId(@PathVariable Long id) {
        logger.info("Buscando cotação com ID: " + id);
        Optional<DadosCotacao> cotacao = dadosCotacaoRepository.findById(id);

        if (cotacao.isPresent()) {
            logger.info("Cotação encontrada: " + cotacao.get());
            return ResponseEntity.ok(cotacao.get());
        } else {
            logger.warning("Cotação não encontrada para o ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cotação não encontrada para o ID: " + id);
        }
    }
}
