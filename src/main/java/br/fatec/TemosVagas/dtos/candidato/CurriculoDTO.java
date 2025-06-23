package br.fatec.TemosVagas.dtos.candidato;

import br.fatec.TemosVagas.entities.candidato.Curriculo;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public record CurriculoDTO(
        List<FormacaoDTO> formacao
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static CurriculoDTO valueOf(Curriculo curriculo) {
        if ( curriculo != null ) {
            return new CurriculoDTO(
                    FormacaoDTO.valueAll(curriculo.getListaFormacao())
            );
        }
        return null;
    }

    public static Curriculo toCurriculo(CurriculoDTO dto) {
        if ( dto != null ) {
            Curriculo curriculo = new Curriculo();
            if (dto.formacao != null) { // Caso um usuário passe a formação diretamente
                curriculo.setListaFormacao(FormacaoDTO.toFormacoes(dto.formacao));
            } else {
                curriculo.setListaFormacao(null);
            }
            return curriculo;
        }
        return null;
    }
}
