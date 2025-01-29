package br.com.seguros.cotacao.domain.repository;

import br.com.seguros.cotacao.domain.model.DadosCotacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DadosCotacaoRepository extends JpaRepository<DadosCotacao, Long> {

}