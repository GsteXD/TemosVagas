package br.fatec.TemosVagas.entities.usuario;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@Data
@MappedSuperclass // Essa classe não vai pro banco, ela só está mapeando as colunas paras as classes que vão herdar dela
public class Usuario implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @EqualsAndHashCode.Include // Caso haja necessidade, é usada essa coluna para comparar usuários
    @SequenceGenerator(
            name = "usuario_seq",
            sequenceName = "public.usuario_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq")
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "telefone", nullable = false, length = 14)
    private String telefone;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "senha", nullable = false, length = 255)
    private String senha;

    // Adição das colunas de endereço no mapeamento da super classe
    @Embedded
    private Endereco endereco;

}
