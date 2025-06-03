package br.fatec.TemosVagas.dtos;

import br.fatec.TemosVagas.entities.candidato.Formacao;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public record FormacaoDTO(
        String curso,
        String instituicao,
        String tipoDiploma,
        Date dataInicio,
        Date dataFim
        
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static FormacaoDTO valueOf(Formacao formacao) {
        if (formacao != null) {
            return new FormacaoDTO(
                    formacao.getCurso(),
                    formacao.getInstituicao(),
                    formacao.getTipoDiploma(),
                    formacao.getDataInicio(),
                    formacao.getDataFim()
            );
        }
        return null;
    }

    public static List<FormacaoDTO> valueAll(List<Formacao> formacoes) {
        if ( formacoes != null ) {
            List<FormacaoDTO> dtos = new ArrayList<>();
            formacoes.forEach(
                    formacao -> dtos.add(valueOf(formacao))
            );
            return dtos;
        }
        return null;
    }

    public static List<Formacao> toFormacao(List<FormacaoDTO> dtos) {
        if (dtos == null || dtos.isEmpty()) return Collections.emptyList();

        return dtos.stream()
                .map(FormacaoDTO::toFormacao)
                .collect(Collectors.toList());
    }

    public static Formacao toFormacao(FormacaoDTO dto) {
        if ( dto != null ) {
            Formacao formacao = new Formacao();
            formacao.setCurso(dto.curso);
            formacao.setInstituicao(dto.instituicao);
            formacao.setTipoDiploma(dto.tipoDiploma);
            formacao.setDataInicio(dto.dataInicio);
            formacao.setDataFim(dto.dataFim);
            return formacao;
        }
        return null;
    }

}
