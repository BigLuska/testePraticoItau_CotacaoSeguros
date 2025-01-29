package br.com.seguros.cotacao.infrastructure.mock.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;
import java.util.List;

@Data
@SuppressWarnings("all") //retirado apontamento de sonar e testes por se tratar de um mock
public class OfertaDTO {

    private String id;

    @JsonProperty("product_id")
    private String productId;

    private String name;

    @JsonProperty("created_at")
    private String createdAt;

    private boolean active;

    private Map<String, BigDecimal> coverages;

    private List<String> assistances;

    @JsonProperty("monthly_premium_amount")
    private PremiumAmountDTO monthlyPremiumAmount;

    private String retorno;

}
