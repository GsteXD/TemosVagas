package br.fatec.TemosVagas.entities.candidato;

import br.fatec.TemosVagas.entities.usuario.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "candidato", schema = "public")
public class Candidato extends Usuario implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Column(name = "cpf", nullable = false, length = 14)
    private String cpf;

}
