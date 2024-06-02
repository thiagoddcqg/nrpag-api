package com.nextreleaseproblem.model.novamodelagem;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NovaModelagemFuncionario {
    private int id;
    private double capacidade;
    private List<String> habilidades;
}
