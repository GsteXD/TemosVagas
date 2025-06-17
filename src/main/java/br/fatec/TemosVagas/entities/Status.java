package br.fatec.TemosVagas.entities;

import java.io.Serial;
import java.io.Serializable;

import br.fatec.TemosVagas.entities.enums.TipoStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "status", schema = "public")
public class Status implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "status", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private TipoStatus Status;
}
