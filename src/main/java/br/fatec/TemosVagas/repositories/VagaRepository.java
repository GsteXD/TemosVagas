package br.fatec.TemosVagas.repositories;

import br.fatec.TemosVagas.entities.enums.TipoStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import br.fatec.TemosVagas.dtos.vaga.VagaRelatorioDTO;
import br.fatec.TemosVagas.entities.Vaga;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VagaRepository extends JpaRepository<Vaga, Long>{

    @Query("SELECT v FROM Vaga v WHERE v.status.Status = :status")
    List<Vaga> findByStatusStatus(@Param("status") TipoStatus status);

    //Verifica se a empresa logada é dona da vaga
    @Query("SELECT v FROM Vaga v WHERE v.id = :id AND v.empresa.id = :empresaId")
    Optional<Vaga> findByIdAndEmpresaId(Long id, Long empresaId);

    //Realiza um relatório das vagas da empresa logada
    @Query("""
        SELECT new br.fatec.TemosVagas.dtos.vaga.VagaRelatorioDTO(
            v.id,
            v.titulo,
            v.empresa.grupo,
            COUNT(a.id)
        )
        FROM Vaga v
        LEFT JOIN Aplicacao a ON a.vaga = v
        WHERE v.empresa.id = :empresaId
        GROUP BY v.id, v.titulo, v.empresa.grupo
    """)
    List<VagaRelatorioDTO> gerarRelatorioEmpresa(@Param("empresaId") Long empresaId);

    //Realiza um relatório global das vagas de uma empresa por grupo
    @Query("""
        SELECT new br.fatec.TemosVagas.dtos.vaga.VagaRelatorioDTO(
            v.id,
            v.titulo,
            v.empresa.grupo,
            COUNT(a.id)
        )
        FROM Vaga v
        LEFT JOIN Aplicacao a ON a.vaga = v
        WHERE v.empresa.grupo = :grupo
        GROUP BY v.id, v.titulo, v.empresa.grupo
    """)
    List<VagaRelatorioDTO> gerarRelatorioGlobalPorGrupo(@Param("grupo") String grupo);
}