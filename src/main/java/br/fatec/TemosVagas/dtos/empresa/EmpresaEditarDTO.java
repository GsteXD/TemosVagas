package br.fatec.TemosVagas.dtos.empresa;

import java.io.Serial;
import java.io.Serializable;

import br.fatec.TemosVagas.dtos.EnderecoDTO;
import br.fatec.TemosVagas.entities.Empresa;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EmpresaEditarDTO(

    @NotBlank(message = "É necessário informar um nome.")
    String nome,

    @NotBlank(message = "É necessário informar um CNPJ.")
    @Pattern(
        regexp = "^(\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}|\\d{14})$",
        message = "O CNPJ inserido não corresponde ao formato desejado."
    )
    String cnpj,

    String grupo,

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

    public static EmpresaEditarDTO valueOf(Empresa empresa) {
        if (empresa != null) {
            return new EmpresaEditarDTO(
                empresa.getNome(), 
                empresa.getCnpj(), 
                empresa.getGrupo(), 
                empresa.getTelefone(), 
                empresa.getEmail(), 
                EnderecoDTO.valueOf(empresa.getEndereco())
            );
        }
        return null;
    }

    public static Empresa toEmpresa(EmpresaEditarDTO dto) {
        if (dto != null) {
            Empresa empresa = new Empresa();
            empresa.setNome(dto.nome);
            empresa.setCnpj(dto.cnpj);
            empresa.setGrupo(dto.grupo);
            empresa.setTelefone(dto.telefone);
            empresa.setEmail(dto.email);
            if (dto.endereco != null) {
                empresa.setEndereco(EnderecoDTO.toEndereco(dto.endereco));
            } else {
                empresa.setEndereco(null);
            }
            return empresa;
        }
        return null;
    }

}
