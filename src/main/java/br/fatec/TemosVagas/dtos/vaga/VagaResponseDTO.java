package br.fatec.TemosVagas.dtos.vaga;

import java.io.Serial;
import java.io.Serializable;

import br.fatec.TemosVagas.entities.Vaga;

public record VagaResponseDTO(
    String nomeEmpresa,
    String status,
    String tipo,
    String titulo,
    String descricao,
    String localidade,
    String dataPub,
    String dataLimite,
    String modalidade,
    String semestre,
    String curso,
    String cursoConclusao

) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static VagaResponseDTO valueOf(Vaga vaga) {
        if (vaga != null) {
            return new VagaResponseDTO(
                vaga.getEmpresa().getNome(),
                vaga.getStatus().getStatus().toString(),
                vaga.getTipo(), 
                vaga.getTitulo(), 
                vaga.getDescricao(), 
                vaga.getLocalidade(),
                vaga.getDataPub(),
                vaga.getDataLimite(), 
                vaga.getModalidade(), 
                vaga.getSemestre(), 
                vaga.getCurso(), 
                vaga.getCursoConclusao()
            );
        }
        return null;
    }   
}
