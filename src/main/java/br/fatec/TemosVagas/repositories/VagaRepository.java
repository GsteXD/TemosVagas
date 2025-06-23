package br.fatec.TemosVagas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.fatec.TemosVagas.entities.Vaga;

public interface VagaRepository extends JpaRepository<Vaga, Long>{
    
} 