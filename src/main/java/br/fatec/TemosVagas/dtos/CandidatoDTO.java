package br.fatec.TemosVagas.dtos;

import br.fatec.TemosVagas.entities.candidato.Candidato;

import java.io.Serial;
import java.io.Serializable;

public record CandidatoDTO(
        String nome,
        String cpf,
        String telefone,
        String email,
        String senha,
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
