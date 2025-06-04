package br.fatec.TemosVagas.dtos;

import br.fatec.TemosVagas.entities.usuario.Endereco;

import java.io.Serial;
import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EnderecoDTO(

        @NotBlank(message = "É necessário informar um país")
        String pais,

        @NotBlank(message = "É necessário informar uma cidade.")
        String cidade,

        @NotBlank(message = "É necessário informar um estado.")
        String estado,

        @NotBlank(message = "É necessário informar um cep.")
        @Pattern(
            regexp = "^(\\d{5}-\\d{3}|\\d{8})$",
            message = "O CEP inserido não corresponde ao formato desejado."
        )
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
