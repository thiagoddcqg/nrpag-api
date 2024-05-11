package com.nextreleaseproblem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetornoDadosDTO {

    private String algoritmo;
    private String htmlString;
    private int quantidadeSemanas;
    private double quantidadeHorasSemana;
    private int quantidadeFeatures;
    private int quantidadeEmpregados;
    private int quantidadeHabitantes;
    private double taxaPrecedencia;

}
