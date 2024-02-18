package com.nextreleaseproblem.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultadoNRP {

    private String algoritmo;
    private double mediaMinimoy = 0.0;
    private double mediaMaximoy = 0.0;
    private double desvioPadraoMinimo = 0.0;
    private double desvioPadraoMaximo = 0.0;
    private double significanciaEstatistica = 0.0;
    private double tempoMedio = 0.0;
    private double proporcao = 0.0;
    private int cont = 0;

}
