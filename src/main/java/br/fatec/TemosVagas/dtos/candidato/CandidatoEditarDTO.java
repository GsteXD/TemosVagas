package br.fatec.TemosVagas.dtos.candidato;

import br.fatec.TemosVagas.dtos.EnderecoDTO;
import br.fatec.TemosVagas.entities.candidato.Candidato;

import java.io.Serial;
import java.io.Serializable;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;;

// DTO para atualização de dados, não inclui senha
public record CandidatoEditarDTO(

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

        @Valid
        EnderecoDTO endereco

) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static CandidatoEditarDTO valueOf(Candidato candidato) {
        if (candidato != null) {
            return new CandidatoEditarDTO(
                    candidato.getNome(),
                    candidato.getCpf(),
                    candidato.getTelefone(),
                    candidato.getEmail(),
                    EnderecoDTO.valueOf(candidato.getEndereco())
            );
        }
        return null;
    }

    public static Candidato toCandidato(CandidatoEditarDTO dto) {
        if (dto != null) {
            Candidato candidato = new Candidato();
            candidato.setNome(dto.nome);
            candidato.setCpf(dto.cpf);
            candidato.setTelefone(dto.telefone);
            candidato.setEmail(dto.email);
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