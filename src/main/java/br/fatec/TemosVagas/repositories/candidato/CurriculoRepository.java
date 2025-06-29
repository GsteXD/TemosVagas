package br.fatec.TemosVagas.repositories.candidato;

import br.fatec.TemosVagas.entities.candidato.Curriculo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CurriculoRepository extends JpaRepository<Curriculo, Long> {

    @Query(value = "SELECT c FROM Curriculo c WHERE c.candidato.id = :candidatoId", nativeQuery = false)
    Optional<Curriculo> findByCandidatoId(Long candidatoId);
}
