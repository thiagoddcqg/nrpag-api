package com.nextreleaseproblem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MetaheuristicaEnum {
    EMPTY("", ""),
    ALGORITMO_GENETICO("Algoritmo Genético", "AG"),
    SIMULATED_ANNEALING("Simulated Annealing", "SA"),
    BUSCA_TABU("Busca Tabu", "BT"),
    GRASP("Greedy Randomized Adaptive Search Procedure", "GRASP");

    private String nome;
    private String sigla;
}
