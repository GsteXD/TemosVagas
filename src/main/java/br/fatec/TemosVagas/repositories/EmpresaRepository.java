package br.fatec.TemosVagas.repositories;

import br.fatec.TemosVagas.entities.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Optional<Empresa> findByEmail (String email);
    Optional<Empresa> findbyCNPJ (String cnpj);

    //TODO: Verificar se há a necessidade de aplicar um query aqui
}
