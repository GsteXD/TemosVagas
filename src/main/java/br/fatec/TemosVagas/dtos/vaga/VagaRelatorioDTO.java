package br.fatec.TemosVagas.dtos.vaga;

import java.io.Serial;
import java.io.Serializable;

public record VagaRelatorioDTO(
    Long vagaId,
    String titulo,
    String grupo,
    Long quantidadeInscritos
) implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;

}
