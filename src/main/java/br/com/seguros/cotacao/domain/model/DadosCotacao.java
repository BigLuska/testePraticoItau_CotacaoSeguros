package br.com.seguros.cotacao.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@Data
@Entity
@Table(name = "dados_cotacao")
public class DadosCotacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long insurance_policy_id;
    private String product_id;
    private String offer_id;
    private String category;
    private BigDecimal total_monthly_premium_amount;
    private BigDecimal total_coverage_amount;

    @ElementCollection
    @CollectionTable(name = "dados_cotacao_coverages", joinColumns = @JoinColumn(name = "dados_cotacao_id"))
    @MapKeyColumn(name = "coverage_key")
    @Column(name = "coverage_value")
    private Map<String, BigDecimal> coverages;

    @ElementCollection
    @CollectionTable(name = "dados_cotacao_assistances", joinColumns = @JoinColumn(name = "dados_cotacao_id"))
    @Column(name = "assistance")
    private List<String> assistances;

    @Embedded
    private Customer customer;

    @Embeddable
    @Data
    public static class Customer {

        private String document_number;
        private String name;
        private String type;
        private String gender;
        private LocalDate date_of_birth;
        private String email;
        private Long phone_number;
    }
}