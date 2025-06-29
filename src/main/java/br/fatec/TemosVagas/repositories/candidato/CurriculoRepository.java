package br.fatec.TemosVagas.repositories.candidato;

import br.fatec.TemosVagas.entities.candidato.Curriculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurriculoRepository extends JpaRepository<Curriculo, Long> {
    Optional<Curriculo> findByCandidato_Id(Long candidatoId);

    boolean existsByCandidato_Id(Long candidatoId);
}
