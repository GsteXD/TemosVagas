package br.fatec.TemosVagas.dtos;

import br.fatec.TemosVagas.entities.usuario.Endereco;

import java.io.Serial;
import java.io.Serializable;

public record EnderecoDTO(
        String pais,
        String cidade,
        String estado,
        String cep
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // Recebe os valores
    public static EnderecoDTO valueOf(Endereco endereco) {
        if (endereco != null) {
            return new EnderecoDTO(
                    endereco.getPais(),
                    endereco.getCidade(),
                    endereco.getEstado(),
                    endereco.getCep()
            );
        }
        return null;
    }

    // Envia os valores
    public static Endereco toEndereco(EnderecoDTO dto) {
        if (dto != null) {
            Endereco endereco = new Endereco();
            endereco.setPais(dto.pais);
            endereco.setCidade(dto.cidade);
            endereco.setEstado(dto.estado);
            endereco.setCep(dto.cep);
            return endereco;
        }
        return null;
    }

}
