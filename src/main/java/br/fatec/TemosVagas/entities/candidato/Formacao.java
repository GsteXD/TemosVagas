package br.fatec.TemosVagas.entities.candidato;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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
    private LocalDate dataInicio;

    @Column(name = "dataFim", nullable = false)
    private LocalDate dataFim;

    @ManyToOne
    @JoinColumn(name = "id_curriculo")
    @JsonBackReference
    private Curriculo curriculo;

    private String MesAnoFormatado(YearMonth yearMonth) {
        return yearMonth.format(DateTimeFormatter.ofPattern("MMMM/yyyy", new Locale("pt", "BR")));
    }

    public String getDataInicio() {
        return MesAnoFormatado(YearMonth.from(dataInicio));
    };

    public String getDataFim() {
        return MesAnoFormatado(YearMonth.from(dataFim));
    }

    public void setDataInicio(YearMonth dataInicio) {
        this.dataInicio = dataInicio.atDay(1);
    }

    public void setDataFim(YearMonth dataFim) {
        this.dataFim = dataFim.atDay(1);
    }

}
