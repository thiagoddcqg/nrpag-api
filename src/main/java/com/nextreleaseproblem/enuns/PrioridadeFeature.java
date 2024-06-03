package com.nextreleaseproblem.enuns;

import lombok.Getter;

@Getter
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
