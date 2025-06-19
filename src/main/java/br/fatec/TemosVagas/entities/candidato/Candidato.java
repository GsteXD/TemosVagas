package br.fatec.TemosVagas.entities.candidato;

import br.fatec.TemosVagas.entities.usuario.Usuario;
import br.fatec.TemosVagas.entities.usuario.UsuarioRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "candidato", schema = "public")
public class Candidato extends Usuario {

    @Serial
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Column(name = "cpf", nullable = false, length = 14)
    private String cpf;

    @PrePersist
    public void prePersist() {
        this.setRole(UsuarioRole.ROLE_CANDIDATO);
    }
}
