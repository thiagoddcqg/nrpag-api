package com.nextreleaseproblem.enuns;

public enum PrioridadeFeature {
    BAIXA(1),
    MEDIA(3),
    ALTA(5),
    URGENTE(10);

    private final int pontuacao;

    PrioridadeFeature(int pontuacao) {
        this.pontuacao = pontuacao;
    }
}
