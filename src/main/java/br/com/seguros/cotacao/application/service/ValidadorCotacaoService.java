package br.com.seguros.cotacao.application.service;

import br.com.seguros.cotacao.domain.model.DadosCotacao;
import br.com.seguros.cotacao.infrastructure.mock.model.OfertaDTO;
import br.com.seguros.cotacao.infrastructure.mock.model.ProdutoDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class ValidadorCotacaoService {
    private static final Logger logger = Logger.getLogger(ValidadorCotacaoService.class.getName());

    public boolean validacaoRegrasNegocio(DadosCotacao dadosCotacao, ProdutoDTO retornoProdutoMock, OfertaDTO retornoOfertaMock) {
        logger.info("Iniciando a validação das regras de negócio.");

        if (isErro(retornoProdutoMock, retornoOfertaMock)) {
            logger.warning("Erro encontrado nos dados dos mocks.");
            return false;
        }

        if (!isAtivo(retornoProdutoMock, retornoOfertaMock)) {
            logger.warning("Produto ou oferta não estão ativos.");
            return false;
        }

        if (!validarCoberturas(dadosCotacao, retornoOfertaMock)) {
            logger.warning("Falha na validação das coberturas.");
            return false;
        }

        if (!validarAssistencias(dadosCotacao, retornoOfertaMock)) {
            logger.warning("Falha na validação das assistências.");
            return false;
        }

        if (!validarPremioMensal(dadosCotacao, retornoOfertaMock)) {
            logger.warning("Falha na validação do prêmio mensal.");
            return false;
        }

        return validarTotalCoberturas(dadosCotacao);
    }

    private boolean isErro(ProdutoDTO retornoProdutoMock, OfertaDTO retornoOfertaMock) {
        return Optional.ofNullable(retornoProdutoMock.getRetorno()).orElse("").contains("erro") ||
                Optional.ofNullable(retornoOfertaMock.getRetorno()).orElse("").contains("erro");
    }

    private boolean isAtivo(ProdutoDTO retornoProdutoMock, OfertaDTO retornoOfertaMock) {
        return retornoOfertaMock.isActive() && retornoProdutoMock.isActive();
    }

    public boolean validarCoberturas(DadosCotacao dadosCotacao, OfertaDTO ofertaDTO) {
        logger.info("Iniciando a validação das coberturas");

        for (Map.Entry<String, BigDecimal> entry : dadosCotacao.getCoverages().entrySet()) {
            if (!validarCobertura(entry.getKey(), entry.getValue(), ofertaDTO)) {
                return false;
            }
        }

        logger.info("Validação concluída: Todas as coberturas são válidas.");
        return true;
    }

    private boolean validarCobertura(String coverageKey, BigDecimal dadosCotacaoCoverageValue, OfertaDTO ofertaDTO) {
        if (!ofertaDTO.getCoverages().containsKey(coverageKey)) {
            logger.warning("Cobertura ausente em OfertaDTO: " + coverageKey);
            return false;
        }

        BigDecimal ofertaCoverageValue = ofertaDTO.getCoverages().get(coverageKey);

        if (dadosCotacaoCoverageValue.compareTo(ofertaCoverageValue) > 0) {
            logger.warning(String.format("Cobertura inválida: [%s]. Valor em DadosCotacao (%s) é maior que em OfertaDTO (%s).",
                    coverageKey, dadosCotacaoCoverageValue, ofertaCoverageValue));
            return false;
        }

        logger.info(String.format("Cobertura válida: [%s]. Valor em DadosCotacao (%s) <= em OfertaDTO (%s).",
                coverageKey, dadosCotacaoCoverageValue, ofertaCoverageValue));
        return true;
    }

    public boolean validarAssistencias(DadosCotacao dadosCotacao, OfertaDTO ofertaDTO) {
        logger.info("Iniciando a validação das assistências");

        for (String assistance : dadosCotacao.getAssistances()) {
            if (!validarAssistencia(assistance, ofertaDTO)) {
                return false;
            }
        }

        logger.info("Validação das assistências concluída: Todas são válidas.");
        return true;
    }

    private boolean validarAssistencia(String assistance, OfertaDTO ofertaDTO) {
        if (!ofertaDTO.getAssistances().contains(assistance)) {
            logger.warning("Assistência ausente em OfertaDTO: " + assistance);
            return false;
        }

        logger.info("Assistência válida: " + assistance);
        return true;
    }

    public boolean validarPremioMensal(DadosCotacao dadosCotacao, OfertaDTO ofertaDTO) {
        logger.info("Iniciando a validação do total_monthly_premium_amount...");

        BigDecimal totalPremium = dadosCotacao.getTotal_monthly_premium_amount();
        BigDecimal minPremium = ofertaDTO.getMonthlyPremiumAmount().getMinAmount();
        BigDecimal maxPremium = ofertaDTO.getMonthlyPremiumAmount().getMaxAmount();

        logger.info(String.format("DadosCotacao total_monthly_premium_amount: %s", totalPremium));
        logger.info(String.format("OfertaDTO monthly_premium_amount (min): %s, (max): %s", minPremium, maxPremium));

        if (totalPremium.compareTo(minPremium) >= 0 && totalPremium.compareTo(maxPremium) <= 0) {
            logger.info("total_monthly_premium_amount está dentro do intervalo.");
            return true;
        } else {
            logger.warning("total_monthly_premium_amount está fora do intervalo.");
            return false;
        }
    }

    public boolean validarTotalCoberturas(DadosCotacao dadosCotacao) {
        logger.info("Iniciando a validação da soma das coberturas...");

        BigDecimal totalCoverageAmount = dadosCotacao.getTotal_coverage_amount();
        BigDecimal coverageSum = dadosCotacao.getCoverages().values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        logger.info(String.format("Total Coverage Amount esperado: %s", totalCoverageAmount));
        logger.info(String.format("Soma das Coberturas calculada: %s", coverageSum));

        if (totalCoverageAmount.compareTo(coverageSum) == 0) {
            logger.info("A soma das coberturas corresponde ao total_coverage_amount.");
            return true;
        } else {
            logger.warning("A soma das coberturas NÃO corresponde ao total_coverage_amount.");
            return false;
        }
    }
}
