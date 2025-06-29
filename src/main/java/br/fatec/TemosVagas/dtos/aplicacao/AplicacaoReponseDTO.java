package br.fatec.TemosVagas.dtos.aplicacao;

import java.io.Serial;
import java.io.Serializable;

public record AplicacaoReponseDTO(
        Long id,
        String nomeCandidato,
        String tituloVaga,
        String nomeEmpresa,
        String dataAplicacao,
        String status,
        String informacoesAdicionais
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 123424343244L;

}
