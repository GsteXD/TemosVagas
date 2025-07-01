package br.fatec.TemosVagas.repositories;

import br.fatec.TemosVagas.entities.Aplicacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AplicacaoRepository extends JpaRepository<Aplicacao, Long> {

    List<Aplicacao> findByCandidato_Id(Long candidatoId);

    List<Aplicacao> findByVaga_Id(Long vagaId);

    Optional<Aplicacao> findByCandidato_IdAndVaga_Id(Long candidatoId, Long vagaId);

}
