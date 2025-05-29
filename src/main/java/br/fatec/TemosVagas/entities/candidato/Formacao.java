package br.fatec.TemosVagas.entities.candidato;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "formacao", schema = "public")
public class Formacao implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(
            name = "formacao_seq",
            sequenceName = "public.formacao_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "formacao_seq")
    private Long id;

    @Column(name = "curso", nullable = false, length = 100)
    private String curso;

    @Column(name = "instituicao", nullable = false, length = 100)
    private String instituicao;

    @Column(name = "tipoDiploma", nullable = false, length = 50)
    private String tipoDiploma;

    @Column(name = "dataInicio", nullable = false)
    private Date dataInicio;

    @Column(name = "dataFim", nullable = false)
    private Date dataFim;

}
