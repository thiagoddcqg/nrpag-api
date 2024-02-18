package com.nextreleaseproblem.model;

import java.util.List;

public class Sprint {
    //Gera os dados para mostrar na tela da próxima sprint
    public static String featureSelecionadaProximaSprint(Release melhorRelease, List<Funcionalidade> funcionalidadeList){
        String recursoBeneficio = "";
        int totalFeatures = 0;
        int totalPontosFuncao = 0;
        for(int i = 0; i < melhorRelease.getFeatures().length; i++){
            if(melhorRelease.getFeatures()[i] == 1){
                recursoBeneficio += "Feature #" + funcionalidadeList.get(i).getId() + " - Prioridade: " + funcionalidadeList.get(i).getPrioridade() + " - Ponto de função: " + funcionalidadeList.get(i).getPontoFuncao() + "\n";
                totalPontosFuncao += funcionalidadeList.get(i).getPontoFuncao();
                totalFeatures++;
            }
        }
        recursoBeneficio += "Total features utilizadas: " + totalFeatures;
        recursoBeneficio += "\nSomatório dos pontos de função: " + totalPontosFuncao;
        return recursoBeneficio;
    }

    //Faz o cálculo de quantas sprints serão necessárias para atender todas as features
    public static int calcularQtdeSprints(int provisao, List<Funcionalidade> funcionalidadeList){
        double resultadoDivisao = (double) Release.somatorioPontosFuncao(funcionalidadeList) / provisao;
        return (int) Math.ceil(resultadoDivisao);
    }

    // Calcula o total de features que foi utilizadas pelas sprints
    public static int calcularTotalUsadas(List<Funcionalidade> funcionalidadeList){
        int cont = 0;
        for(Funcionalidade funcionalidade : funcionalidadeList){
            if(funcionalidade.isUsada()){
                cont++;
            }
        }
        return cont;
    }

    //Monsta os dados a serem exibidos no text area Features usadas por sprint
    public static String montarRetornoSprints(Release release, String numeroSprint, List<Funcionalidade> funcionalidadeList, int provisao, Integer  qtdAcessoFuncaoObjetiva){
        String retorno = "################ - Sprint " + numeroSprint + " - ################\n";
        int cont = 0;
        for(int i = 0; i < release.getFeatures().length; i++){
            if(release.getFeatures()[i] == 1){
                retorno += "Feature: " + funcionalidadeList.get(i).getId() + "\n";
                cont ++;
            }
        }
        retorno += "Total features utilizadas: " + (cont);
        retorno += "\nSomatório dos pontos de aptidão da sprint: " + Release.funcaoObjetivo(release.getFeatures(), funcionalidadeList,  qtdAcessoFuncaoObjetiva)[0];
        retorno += "\nSomatório dos pontos de função da sprint: " + Release.totalPontoFuncao(release.getFeatures(), funcionalidadeList);
        retorno += "\nTaxa de aproveitamento da provisão na sprint: " + Release.calcularTaxaAproveitamentoProvisao(Release.totalPontoFuncao(release.getFeatures(), funcionalidadeList), provisao) + "\n";
        return retorno;
    }
}
