package br.fatec.TemosVagas.entities.usuario;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Embeddable // Considerei não utilizar uma entidade pela falta de necessidade de normalizar (considerando que só teriamos um usuário por empresa)
public class Endereco implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "pais", nullable = false, length = 100)
    private String pais;

    @Column(name = "cidade", nullable = false, length = 100)
    private String cidade;

    @Column(name = "estado", nullable = false, length = 50)
    private String estado;

    @Column(name = "cep", nullable = false, length = 9)
    private String cep;

}
