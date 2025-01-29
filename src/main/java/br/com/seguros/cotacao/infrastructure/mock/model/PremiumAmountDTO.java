package br.com.seguros.cotacao.infrastructure.mock.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@SuppressWarnings("all") //retirado apontamento de sonar e testes por se tratar de um mock
public class PremiumAmountDTO {

    @JsonProperty("max_amount")
    private BigDecimal maxAmount;

    @JsonProperty("min_amount")
    private BigDecimal minAmount;

    @JsonProperty("suggested_amount")
    private BigDecimal suggestedAmount;
}

