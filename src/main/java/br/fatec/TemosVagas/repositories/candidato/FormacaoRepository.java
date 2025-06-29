package br.fatec.TemosVagas.repositories.candidato;

import br.fatec.TemosVagas.entities.candidato.Formacao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FormacaoRepository extends JpaRepository<Formacao, Long> {

    //Query para buscar uma formação através do ID da formação e do ID do currículo
    //Como o currículo obedece ao contexto do usuário logado, é possível garantir que a formação pertence ao currículo do usuário
    @Query("SELECT f FROM Formacao f WHERE f.id = :formacaoId AND f.curriculo.id = :curriculoId")
    Optional<Formacao> findByIdAndCurriculoId(Long formacaoId, Long curriculoId);
}
