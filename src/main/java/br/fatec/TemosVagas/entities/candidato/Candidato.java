package br.fatec.TemosVagas.entities.candidato;

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
@Table(name = "candidato", schema = "public")
public class Candidato extends Usuario {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @EqualsAndHashCode.Include // Caso haja necessidade, é usada essa coluna para comparar usuários
    @SequenceGenerator(
            name = "candidato_seq",
            sequenceName = "public.candidato_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "candidato_seq")
    private Long id;

    @EqualsAndHashCode.Include
    @Column(name = "cpf", nullable = false, length = 14)
    private String cpf;

    @PrePersist
    public void prePersist() {
        this.setRole(UsuarioRole.ROLE_CANDIDATO);
    }
}
