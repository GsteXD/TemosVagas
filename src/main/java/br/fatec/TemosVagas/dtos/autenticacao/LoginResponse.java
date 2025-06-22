package br.fatec.TemosVagas.dtos.autenticacao;

public record LoginResponse(
        String token,
        String nome,
        String role
) {
}
