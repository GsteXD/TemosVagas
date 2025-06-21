package br.fatec.TemosVagas.entities;

import br.fatec.TemosVagas.entities.usuario.Usuario;
import br.fatec.TemosVagas.entities.enums.UsuarioRole;
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
@Table(name = "empresa", schema = "public")
public class Empresa extends Usuario {

    @Serial
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Column(name = "cnpj", nullable = false, length = 18)
    private String cnpj;

    @Column(name = "grupo", nullable = false, length = 100)
    private String grupo;


    @PrePersist
    public void prePersist() {
        this.setRole(UsuarioRole.ROLE_EMPRESA);
    }
}
