package br.fatec.TemosVagas.dtos.autenticacao;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank (message = "O campo de email não pode estar vazio")
        @Email
        String email,

        @NotBlank (message = "A senha não pode estar vazia")
        String senha
) {}
