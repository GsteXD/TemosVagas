package br.fatec.TemosVagas.entities;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "vaga", schema = "public")
public class Vaga implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(
        name = "vaga_seq",
        sequenceName = "public.vaga_seq",
        allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vaga_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "status", referencedColumnName = "status", nullable = false)
    private Status status;

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;

    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;

    @Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "localidade", nullable = false, length = 255)
    private String localidade;

    @Column(name = "dataPub", nullable = false)
    private LocalDate dataPub;

    @Column(name = "dataLimite", nullable = false)
    private LocalDate dataLimite;

    @Column(name = "modalidade", nullable = false, length = 30)
    private String modalidade;

    @Column(name = "semestre", nullable = true, length = 20)
    private String semestre;

    @Column(name = "curso", nullable = true, length = 80)
    private String curso;

    @Column(name = "cursoConclusao", nullable = true)
    private Year cursoConclusao;

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    public String getDataPub() {
        return dataPub != null
            ? dataPub.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            : null;
    }

    public String getDataLimite() {
        return dataLimite != null
            ? dataLimite.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            : null;
    }

    public String getCursoConclusao() {
        return cursoConclusao != null ? cursoConclusao.toString() : null;
    }

    public void setDataPub(String data) {
        this.dataPub = data != null
            ? LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            : null;
    }

    public void setDataLimite(String data) {
        this.dataLimite = data != null
            ? LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            : null;
    }

    public void setCursoConclusao(String data) {
        this.cursoConclusao = data != null ? Year.parse(data) : null;
    }

}
