package br.fatec.TemosVagas.dtos.aplicacao;

import br.fatec.TemosVagas.entities.Aplicacao;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;

public record AplicacaoDTO(

        @NotNull(message = "Obrigatorio informar o ID da vaga.")
        Long vagaId,

        String informacaoAdicionais
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 4234324234231L;

    public static AplicacaoReponseDTO valueOf(Aplicacao aplicacao) {
        if(aplicacao != null) {
            return new AplicacaoReponseDTO(
                    aplicacao.getId(),
                    aplicacao.getCandidato().getNome(),
                    aplicacao.getVaga().getTitulo(),
                    aplicacao.getVaga().getEmpresa().getNome(),
                    aplicacao.getDataAplicacao().toString(),
                    aplicacao.getStatusAplicacao().name(),
                    aplicacao.getInformacoesAdicionais()
            );
        }
        return null;
    }
}
