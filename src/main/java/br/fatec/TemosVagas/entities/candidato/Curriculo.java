package br.fatec.TemosVagas.entities.candidato;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
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

    @Lob
    private byte[] arquivoPdf;

    @OneToMany(mappedBy = "curriculo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Formacao> listaFormacao = new ArrayList<>();

    @OneToOne
    @JoinColumn(
            name = "id_candidato",
            referencedColumnName = "id",
            insertable = true,
            updatable = true
    )
    private Candidato candidato;

}
