package br.fatec.TemosVagas.entities;

import br.fatec.TemosVagas.entities.candidato.Candidato;
import br.fatec.TemosVagas.entities.enums.StatusAplicacao;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "aplicacao", schema = "public")
public class Aplicacao implements Serializable {

    @Serial
    private static final long serialVersionUID = 2131434123343L;

    @Id
    @SequenceGenerator(
            name = "aplicacao_seq",
            sequenceName = "public.aplicacao_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aplicacao_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidato_id", nullable = false)
    private Candidato candidato;

    @ManyToOne
    @JoinColumn(name = "vaga_id", nullable = false)
    private Vaga vaga;

    @Column(name = "data_aplicacao", nullable = false)
    @JsonFormat(pattern = "dd/mm/yyyy HH:mm:ss")
    private LocalDateTime dataAplicacao;

    @Column(name = "informacoes_adicionais", columnDefinition = "TEXT")
    private String informacoesAdicionais;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_aplicacao", nullable = false)
    private StatusAplicacao statusAplicacao;

    @PrePersist
    public void prePersist() {
        this.dataAplicacao = LocalDateTime.now();
        this.statusAplicacao = StatusAplicacao.PENDENTE;
    }
}
