package com.nextreleaseproblem.service.novamodelagem;

import com.nextreleaseproblem.model.novamodelagem.NovaModelagemFeature;
import com.nextreleaseproblem.model.novamodelagem.NovaModelagemNRPModelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NovaModelagemAlgoritmoGenetico {
    private NovaModelagemNRPModelo modelo;
    private int tamanhoPopulacao;
    private double taxaCruzamento;
    private double taxaMutacao;
    private int geracoes;

    public NovaModelagemAlgoritmoGenetico(NovaModelagemNRPModelo modelo, int tamanhoPopulacao, double taxaCruzamento, double taxaMutacao, int geracoes) {
        this.modelo = modelo;
        this.tamanhoPopulacao = tamanhoPopulacao;
        this.taxaCruzamento = taxaCruzamento;
        this.taxaMutacao = taxaMutacao;
        this.geracoes = geracoes;
    }

    public List<NovaModelagemFeature> executar() {
        List<List<NovaModelagemFeature>> populacao = inicializarPopulacao();
        for (int geracao = 0; geracao < geracoes; geracao++) {
            populacao = evoluir(populacao);
        }
        return getMelhorSolucao(populacao);
    }

    private List<List<NovaModelagemFeature>> inicializarPopulacao() {
        List<List<NovaModelagemFeature>> populacao = new ArrayList<>();
        for (int i = 0; i < tamanhoPopulacao; i++) {
            populacao.add(solucaoAleatoria());
        }
        return populacao;
    }

    private List<NovaModelagemFeature> solucaoAleatoria() {
        List<NovaModelagemFeature> solucao = new ArrayList<>();
        Random aleatorio = new Random();
        for (NovaModelagemFeature feature : modelo.getFeatures()) {
            if (aleatorio.nextBoolean()) {
                solucao.add(feature);
            }
        }
        return solucao;
    }

    private List<List<NovaModelagemFeature>> evoluir(List<List<NovaModelagemFeature>> populacao) {
        List<List<NovaModelagemFeature>> novaPopulacao = new ArrayList<>();
        for (int i = 0; i < tamanhoPopulacao; i++) {
            List<NovaModelagemFeature> pai1 = selecionarPai(populacao);
            List<NovaModelagemFeature> pai2 = selecionarPai(populacao);
            List<NovaModelagemFeature> filho = cruzamento(pai1, pai2);
            mutar(filho);
            novaPopulacao.add(filho);
        }
        return novaPopulacao;
    }

    private List<NovaModelagemFeature> selecionarPai(List<List<NovaModelagemFeature>> populacao) {
        Random aleatorio = new Random();
        return populacao.get(aleatorio.nextInt(tamanhoPopulacao));
    }

    private List<NovaModelagemFeature> cruzamento(List<NovaModelagemFeature> pai1, List<NovaModelagemFeature> pai2) {
        List<NovaModelagemFeature> filho = new ArrayList<>();
        Random aleatorio = new Random();
        for (int i = 0; i < pai1.size(); i++) {
            if (aleatorio.nextDouble() < taxaCruzamento) {
                filho.add(pai1.get(i));
            } else if(pai2.size() > i) {
                filho.add(pai2.get(i));
            } else {
                filho.add(pai1.get(i));
            }
        }
        return filho;
    }

    private void mutar(List<NovaModelagemFeature> solution) {
        Random aleatorio = new Random();
        for (int i = 0; i < solution.size(); i++) {
            if (aleatorio.nextDouble() < taxaMutacao) {
                solution.set(i, modelo.getFeatures().get(aleatorio.nextInt(modelo.getFeatures().size())));
            }
        }
    }

    private List<NovaModelagemFeature> getMelhorSolucao(List<List<NovaModelagemFeature>> populacao) {
        List<NovaModelagemFeature> melhorSolucao = null;
        double melhorValor = -Double.MAX_VALUE;
        for (List<NovaModelagemFeature> solucao : populacao) {
            double valor = calcularValor(solucao);
            if (valor > melhorValor) {
                melhorValor = valor;
                melhorSolucao = solucao;
            }
        }
        return melhorSolucao;
    }

    private double calcularValor(List<NovaModelagemFeature> solucao) {
        double valor = 0;
        double esforco = 0;
        for (NovaModelagemFeature feature : solucao) {
            valor += feature.getValorNegocio();
            esforco += feature.getEsforco();
        }
        if (esforco > modelo.getMaximoEsforco() || solucao.size() > modelo.getMaximoFeatures()) {
            return -Double.MAX_VALUE;
        }
        return valor;
    }
}
