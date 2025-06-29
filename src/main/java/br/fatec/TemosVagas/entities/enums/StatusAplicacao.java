package br.fatec.TemosVagas.entities.enums;

public enum StatusAplicacao {

    PENDENTE("Pendente"),
    EM_ANALISE("Em Análise"),
    APROVADO("Aprovado"),
    REJEITADO("Rejeitado"),
    CANCELADO("Cancelado");

    private final String descricao;

    StatusAplicacao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}