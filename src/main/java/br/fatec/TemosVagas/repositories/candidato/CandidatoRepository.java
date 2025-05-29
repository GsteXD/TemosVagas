package br.fatec.TemosVagas.repositories.candidato;

import br.fatec.TemosVagas.entities.candidato.Candidato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CandidatoRepository extends JpaRepository<Candidato, Long> {

    @Query(value = "SELECT c FROM Candidato c WHERE c.email = :email", nativeQuery = false)
    Optional<Candidato> findByEmail(String email);
}
