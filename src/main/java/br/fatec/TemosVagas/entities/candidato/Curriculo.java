package br.fatec.TemosVagas.entities.candidato;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "curriculo", schema = "public")
public class Curriculo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(
            name = "curriculo_seq",
            sequenceName = "public.curriculo_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "curriculo_seq")
    private Long id;

    /* Instância das formações do candidato.
    *  CascadeType.ALL indica que operações CRUD serão automaticamente tratadas
    *  OrphanRemoval compara e remove objetos que não batem com o banco de dados
    */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(
            name = "id_curriculo",
            referencedColumnName = "id",
            insertable = true,
            updatable = true
    )
    private List<Formacao> listaFormacao;

    @OneToOne
    @JoinColumn(
            name = "id_candidato",
            referencedColumnName = "id",
            insertable = true,
            updatable = true
    )
    private Candidato candidato;

}
