package com.nextreleaseproblem.model;

import lombok.Data;

@Data
public class Funcionalidade {

    public Funcionalidade(){
        this.atribuido = false;
        this.usada = false;
    }

    private String id;
    private String sistemaModulo;
    private String projetoModulo;
    private String numeroProjetoHierarquia;
    private String projetoHierarquia;
    private String tipo;
    private String situacao;
    private String titulo;
    private String atribuidoPara;
    private String catalogo;
    private String het;
    private String qtdServicos;
    private String inicio;
    private String tarefaPai;
    private String qtdAnexos;
    private int pontoFuncao;
    private NivelPrioridade prioridade;
    private boolean atribuido;
    private boolean usada;

    public static NivelPrioridade getNivelPrioridade(int level) {
        return NivelPrioridade.getNivelPrioridade(level);
    }

    public Funcionalidade(String id, int pontoFuncao, NivelPrioridade prioridade) {
        this.id = id;
        this.pontoFuncao = pontoFuncao;
        this.prioridade = prioridade;
    }
}