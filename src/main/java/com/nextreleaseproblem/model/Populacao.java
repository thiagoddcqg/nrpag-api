package com.nextreleaseproblem.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
public class Populacao {
    private List<Release> releases;
    private static final Random aleatorio = new Random();

    public Populacao() {
        releases = new ArrayList<>();
    }

    public int getTotalAptidao(List<Funcionalidade> funcionalidadeList, int qtdAcessoFuncaoObjetiva){
        int soma = 0;
        for(Release release : releases){
            soma += Release.funcaoObjetivo(release.getFeatures(), funcionalidadeList, qtdAcessoFuncaoObjetiva)[0];
        }
        return soma;
    }

    //Cria a população inicial a ser utilizada.
    public static Populacao criarPopulacao(int tamanhoPopulacao, List<Funcionalidade> funcionalidadeList, int provisao){
        Populacao populacao = new Populacao();
        while (populacao.getReleases().size() < tamanhoPopulacao) {
            Release release = new Release(funcionalidadeList);
            release.setFeatures(Release.inicializar(provisao, funcionalidadeList));
            populacao.getReleases().add(release);
        }
        return populacao;
    }

    //Método de otimização, serve para aidicionar mais itens a população,
    // caso ainda não tenha atingido sua capacidade máxima de features.
    public static void readequarPopulacao(Populacao populacao, List<Funcionalidade> funcionalidadeList, int provisao){
        for(Release release : populacao.getReleases()){
            int totalPontoFuncao = Release.totalPontoFuncao(release.getFeatures(), funcionalidadeList);
            for(int i = 0; i < release.getFeatures().length; i++ ){
                if(release.getFeatures()[i] == 0 && !funcionalidadeList.get(i).isUsada()){
                    if((totalPontoFuncao + funcionalidadeList.get(i).getPontoFuncao()) > provisao){
                        break;
                    }else{
                        release.getFeatures()[i] = 1;
                        totalPontoFuncao = Release.totalPontoFuncao(release.getFeatures(), funcionalidadeList);
                    }
                }
            }
            release.setFeatures(Release.calcularRestricao(provisao, release.getFeatures(), funcionalidadeList));
        }
    }
}
