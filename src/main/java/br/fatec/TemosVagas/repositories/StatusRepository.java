package br.fatec.TemosVagas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.fatec.TemosVagas.entities.Status;
import br.fatec.TemosVagas.entities.enums.TipoStatus;

public interface StatusRepository extends JpaRepository<Status, TipoStatus> {      
}
