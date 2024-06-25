package com.nextreleaseproblem.dto;

import lombok.Data;

@Data
public class RequestGerarDadosDTO {

    private int numeroDeFeatures;
    private int numeroDeEmpregados;
    private int numeroDeHabilidades;
    private double taxaDeRestricoesDePrecedencia;

}
