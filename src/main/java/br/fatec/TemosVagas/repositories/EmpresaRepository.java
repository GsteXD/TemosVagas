package br.fatec.TemosVagas.repositories;

import br.fatec.TemosVagas.entities.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    //TODO: Verificar se há a necessidade de aplicar um query aqui
}
