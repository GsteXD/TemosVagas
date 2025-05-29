package br.fatec.TemosVagas.repositories.candidato;

import br.fatec.TemosVagas.entities.candidato.Formacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormacaoRepository extends JpaRepository<Formacao, Long> {
}
