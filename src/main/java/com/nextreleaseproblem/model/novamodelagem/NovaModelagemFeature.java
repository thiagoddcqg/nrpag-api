package com.nextreleaseproblem.model.novamodelagem;

import com.nextreleaseproblem.enuns.PrioridadeFeature;
import com.nextreleaseproblem.enuns.StatusFeature;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class NovaModelagemFeature {
    private int id;
    private double valorNegocio;
    private double esforco;
    private List<Integer> precedencia;
    private String tipo;
    private StatusFeature status;
    private String titulo;
    private String atribuidoPara;
    private int quantidadeServico;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private int tarefaPai;
    private int quantidadeAnexos;
    private PrioridadeFeature prioridade;
}