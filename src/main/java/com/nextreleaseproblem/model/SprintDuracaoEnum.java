package com.nextreleaseproblem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SprintDuracaoEnum {
    ALGORITMO_GENETICO("Uma semana"),
    SIMULATED_ANNEALING("Duas semanas"),
    BUSCA_TABU("Três semanas"),
    GRASP("Quatro semanas");

    private String descricao;
}
