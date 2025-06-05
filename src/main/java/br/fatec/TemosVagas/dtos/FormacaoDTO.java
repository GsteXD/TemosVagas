package br.fatec.TemosVagas.dtos;

import br.fatec.TemosVagas.entities.candidato.Formacao;

import java.io.Serial;
import java.io.Serializable;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record FormacaoDTO(
        
        @NotBlank(message = "É necessário informar um curso.")
        String curso,

        @NotBlank(message = "É necessário informar uma instituição.")
        String instituicao,

        @NotBlank(message = "É necessário informar o tipo de diploma.")
        String tipoDiploma,

        @NotBlank(message = "É necessário informar uma data de início.")
        @Pattern(
            regexp = "^(0[1-9]|1[0-2])/\\d{4}$",
            message = "A data deve estar no formato MM/yyyy."
        )
        String dataInicio,

        @NotBlank(message = "É necessário informar uma data de conclusão.")
        @Pattern(
            regexp = "^(0[1-9]|1[0-2])/\\d{4}$",
            message = "A data deve estar no formato MM/yyyy."
        )
        String dataFim
        
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // Exibe uma única formação
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

    public static Formacao toFormacao(FormacaoDTO dto) {
        // Formatação da data para: mês/ano
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
        if ( dto != null ) {
            Formacao formacao = new Formacao();
            formacao.setCurso(dto.curso);
            formacao.setInstituicao(dto.instituicao);
            formacao.setTipoDiploma(dto.tipoDiploma);
            formacao.setDataInicio(YearMonth.parse(dto.dataInicio, formatter));
            formacao.setDataFim(YearMonth.parse(dto.dataFim, formatter));
            return formacao;
        }
        return null;
    }

    // Exibe uma lista de formações
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

    public static List<Formacao> toFormacoes(List<FormacaoDTO> dtos) {
        if (dtos == null || dtos.isEmpty()) return Collections.emptyList();

        return dtos.stream()
                .map(FormacaoDTO::toFormacao)
                .collect(Collectors.toList());
    }

}
