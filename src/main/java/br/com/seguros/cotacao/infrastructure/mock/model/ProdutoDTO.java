package br.com.seguros.cotacao.infrastructure.mock.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@SuppressWarnings("all") //retirado apontamento de sonar e testes por se tratar de um mock
public class ProdutoDTO {
    private String id;
    private String name;
    @JsonProperty("created_at")
    private String createdAt;
    private boolean active;
    private List<String> offers;
    private String retorno;
}
