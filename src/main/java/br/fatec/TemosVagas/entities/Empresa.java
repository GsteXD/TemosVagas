package br.fatec.TemosVagas.entities;

import br.fatec.TemosVagas.entities.usuario.Usuario;
import br.fatec.TemosVagas.entities.enums.UsuarioRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
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

    @Id
    @EqualsAndHashCode.Include // Caso haja necessidade, é usada essa coluna para comparar usuários
    @SequenceGenerator(
            name = "empresa_seq",
            sequenceName = "public.empresa_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "empresa_seq")
    private Long id;

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
