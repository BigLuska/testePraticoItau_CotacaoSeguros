package br.com.seguros.cotacao.application.service;

import br.com.seguros.cotacao.domain.model.DadosCotacao;
import br.com.seguros.cotacao.infrastructure.mock.model.OfertaDTO;
import br.com.seguros.cotacao.infrastructure.mock.model.ProdutoDTO;
import br.com.seguros.cotacao.infrastructure.mock.service.ConsultaProdutoServiceMock;
import br.com.seguros.cotacao.domain.repository.DadosCotacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@Service
public class CotacaoService {

    private static final Logger logger = Logger.getLogger(CotacaoService.class.getName());

    @Autowired
    public ValidadorCotacaoService validadorCotacaoService;

    @Autowired
    public DadosCotacaoRepository dadosCotacaoRepository;

    private final MessageSender messageSender;

    @Autowired
    public CotacaoService(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public boolean solicitaCotacao(DadosCotacao dadosCotacao) {
        logger.info("Iniciando a solicitação de cotação.");

        RestTemplate restTemplate = new RestTemplate();
        ConsultaProdutoServiceMock consultaProdutoService = new ConsultaProdutoServiceMock(restTemplate);

        boolean cotacaoValida = validadorCotacaoService.validacaoRegrasNegocio(dadosCotacao,
                consultaProdutoService.consultaProdCatalog(dadosCotacao.getProduct_id(), ProdutoDTO.class),
                consultaProdutoService.consultaOfertaCatalog(dadosCotacao.getOffer_id(), OfertaDTO.class));

        if (cotacaoValida) {
            dadosCotacao = dadosCotacaoRepository.save(dadosCotacao);
            String mensagem = "Cotação recebida: id_cotacao: " + dadosCotacao.getId().toString();

            logger.info("Cotação válida, enviando mensagem.");
            messageSender.sendMessage(mensagem);
        } else {
            logger.warning("Cotação inválida.");
            return false;
        }

        logger.info("Solicitação de cotação concluída com sucesso.");
        return true;
    }

    public void gravaApoliceCotacao(Long idCotacao, Long idApolice) {
        logger.info("Iniciando a gravação da apólice para a cotação.");

        try {
            DadosCotacao dadosCotacao = dadosCotacaoRepository.findById(idCotacao)
                    .orElseThrow(() -> new RuntimeException("Cotação não encontrada com ID: " + idCotacao));

            dadosCotacao.setInsurance_policy_id(idApolice);
            dadosCotacaoRepository.save(dadosCotacao);

            logger.info("Insurance policy ID atualizado com sucesso para a cotação: " + idCotacao);
        } catch (NumberFormatException ex) {
            logger.severe("Erro ao converter ID de cotação ou apólice: " + ex.getMessage());
        } catch (Exception ex) {
            logger.severe("Erro ao gravar apólice de cotação: " + ex.getMessage());
        }
    }
}
