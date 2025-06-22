package br.fatec.TemosVagas.dtos;

import br.fatec.TemosVagas.entities.candidato.Candidato;

import java.io.Serial;
import java.io.Serializable;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;;

public record CandidatoDTO(

        @NotBlank(message = "É necessário informar um nome.")
        String nome,

        @NotBlank(message = "É necessário informar um cpf.")
        @Pattern(
            regexp = "^(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11})$",
            message = "O CPF inserido não corresponde ao formato desejado." 
        )
        String cpf,

        @NotBlank(message = "É necessário informar um telefone.")
        @Pattern(
            regexp = "^(\\(\\d{2}\\)\\s?)?(9\\d{4}|[2-9]\\d{3})-?\\d{4}$",
            message = "O telefone inserido não corresponde ao formato desejado."
        )
        String telefone,

        @NotBlank(message = "É necessário informar um email.")
        String email,

        @NotBlank(message = "É necessário informar uma senha.")
        String senha,

        @Valid
        EnderecoDTO endereco

) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // Recebe os valores
    public static CandidatoDTO valueOf(Candidato candidato) {
        if (candidato != null) {
            return new CandidatoDTO(
                    candidato.getNome(),
                    candidato.getCpf(),
                    candidato.getTelefone(),
                    candidato.getEmail(),
                    candidato.getSenha(),
                    EnderecoDTO.valueOf(candidato.getEndereco())
            );
        }
        return null;
    }

    // Envia os valores
    public static Candidato toCandidato(CandidatoDTO dto) {
        if (dto != null) {
            Candidato candidato = new Candidato();
            candidato.setNome(dto.nome);
            candidato.setCpf(dto.cpf);
            candidato.setTelefone(dto.telefone);
            candidato.setEmail(dto.email);
            candidato.setSenha(dto.senha);
            if (dto.endereco != null) {
                candidato.setEndereco(EnderecoDTO.toEndereco(dto.endereco));
            } else {
                candidato.setEndereco(null);
            }
            return candidato;
        }
        return null;
    }

}
