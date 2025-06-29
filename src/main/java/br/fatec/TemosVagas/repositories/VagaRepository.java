package br.fatec.TemosVagas.repositories;

import br.fatec.TemosVagas.entities.enums.TipoStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import br.fatec.TemosVagas.entities.Vaga;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VagaRepository extends JpaRepository<Vaga, Long>{

    @Query("SELECT v FROM Vaga v WHERE v.status.Status = :status")
    List<Vaga> findByStatusStatus(@Param("status") TipoStatus status);

}