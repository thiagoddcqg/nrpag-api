package com.nextreleaseproblem.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Desenvolvedor {

    private String nome;
    private int disponibilidadeSemanal;
    private NivelDesenvolvedor nivelDesenvolvedor;
    private List<Funcionalidade> funcionalidadeList;

    public Desenvolvedor(){
        funcionalidadeList = new ArrayList<Funcionalidade>();
    }

    public int getProdutividade(){
        return nivelDesenvolvedor.getFator() * disponibilidadeSemanal;
    }

    public int getTotalExforco(){
        int total = 0;
        for(Funcionalidade funcionalidade : funcionalidadeList){
            total += funcionalidade.getPontoFuncao();
        }
        return total;
    }

    public void limpar(){
        funcionalidadeList = new ArrayList<Funcionalidade>();
    }

    @Override
    public String toString(){
        String retorno = nome + " - nível: " + nivelDesenvolvedor.toString() + " - disponibilidade: " + disponibilidadeSemanal + " - pontos de função: " + getTotalExforco() + " de " + getProdutividade();
        for(Funcionalidade funcionalidade : funcionalidadeList){
            retorno += "\n\t" + "Feature: " + funcionalidade.getId() + " - ponto de função: " + funcionalidade.getPontoFuncao();
        }
        return retorno;
    }
}
