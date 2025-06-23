package br.fatec.TemosVagas.dtos.vaga;

import java.io.Serial;
import java.io.Serializable;

import br.fatec.TemosVagas.entities.Vaga;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record VagaDTO(

    @NotBlank(message = "É necessário informar um tipo de vaga.")
    String tipo,

    @NotBlank(message = "É necessário informar um título.")
    String titulo,

    @NotBlank(message = "É necessário informar uma descrição.")
    String descricao,

    @NotBlank(message = "É necessário informar uma localidade.")
    String localidade,

    @NotBlank(message = "É necessário informar uma data limite.")
    @Pattern(
        regexp = "^\\d{2}/\\d{2}/\\d{4}$", 
        message = "A data deve estar no formato dd/MM/yyyy."
    )
    String dataLimite,

    @NotBlank(message = "É necessário informar uma modalidade.")
    String modalidade,

    String semestre,

    String curso,

    @Pattern(
        regexp = "^\\d{4}$", 
        message = "Informe apenas o ano de conclusão (yyyy)."
    )
    String cursoConclusao

) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static Vaga toVaga(VagaDTO dto) {
        if (dto != null) {
            Vaga vaga = new Vaga();
            vaga.setTipo(dto.tipo);
            vaga.setTitulo(dto.titulo);
            vaga.setDescricao(dto.descricao);
            vaga.setLocalidade(dto.localidade);
            vaga.setDataLimite(dto.dataLimite);
            vaga.setModalidade(dto.modalidade);
            vaga.setSemestre(dto.semestre);
            vaga.setCurso(dto.curso);
            vaga.setCursoConclusao(dto.cursoConclusao);
            return vaga;
        }
        return null;
    }

}
