package com.nextreleaseproblem.model.novamodelagem;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NovaModelagemNRPModelo {
    private List<NovaModelagemFeature> features;
    private List<NovaModelagemFuncionario> funcionarios;
    private double maximoFeatures;
    private double maximoEsforco;
}