package br.fatec.TemosVagas.entities.enums;

public enum TipoStatus {

    ABERTO("Vaga Aberta"),
    FECHADO("Vaga Fechada"),
    PRORROGADO("Vaga Prorrogada");

    private final String descricao;

    private TipoStatus(String descricao) { this.descricao = descricao; }

    @Override
    public String toString() { return descricao; }
}