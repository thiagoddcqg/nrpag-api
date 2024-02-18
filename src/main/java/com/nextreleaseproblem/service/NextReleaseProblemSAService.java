package com.nextreleaseproblem.service;

import com.nextreleaseproblem.model.Desenvolvedor;
import com.nextreleaseproblem.model.Funcionalidade;
import com.nextreleaseproblem.model.Release;
import com.nextreleaseproblem.model.Sprint;
import com.nextreleaseproblem.util.GraphUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class NextReleaseProblemSAService {
    private double temperaturaInicial = 0; //100.0
    private double taxaResfriamento = 0; //0.95
    private int numeroIteracoes = 0; //1000
    private int tamanhoSolucao = 0; //10
    private int limiteRecursos = 0; // Limite hipotético de recursos selecionados
    private int qtdeSprints = 0;
    private Release melhorReleaseSprint = null;
    private List<String> retornoLista = new ArrayList<String>();
    private List<Funcionalidade> funcionalidadeList = new ArrayList<Funcionalidade>();
    private List<Desenvolvedor> desenvolvedores;
    private int provisao = 0;

    private int qtdAcessoFuncaoObjetiva = 0;


    public List<String> iniciar(List<Funcionalidade> funcionalidadeList, String temperaturainicialinput,
                                String taxaresfriamentoinput, String numeroiteracoesinput, int duracaoSprint) {
        limparDados();

        temperaturaInicial = Double.parseDouble(temperaturainicialinput);
        taxaResfriamento = Double.parseDouble(taxaresfriamentoinput);
        numeroIteracoes = Integer.parseInt(numeroiteracoesinput);
        tamanhoSolucao = funcionalidadeList.size();
        this.funcionalidadeList = Release.gerarFeatures(funcionalidadeList);
        desenvolvedores = Release.gerarDesenvolvedores(this.funcionalidadeList);
        provisao = Release.calcularProvisao(desenvolvedores, duracaoSprint);
        qtdeSprints = Sprint.calcularQtdeSprints(provisao, funcionalidadeList);
        int[] qtdeFeaturesPorPrioridade = Release.qtdeFeatures(this.funcionalidadeList);

        int[] solucaoInicial = Release.inicializar(provisao, this.funcionalidadeList);
        int[] solucaoObjetiva = Release.funcaoObjetivo(solucaoInicial, this.funcionalidadeList, qtdAcessoFuncaoObjetiva);
        Release releaseInicial = new Release(solucaoInicial, solucaoObjetiva[0]);
        qtdAcessoFuncaoObjetiva = solucaoObjetiva[1];
        Release solucaoOtima = releaseInicial;
        int[] solucaoObjetiva2 = Release.funcaoObjetivo(solucaoInicial, this.funcionalidadeList, qtdAcessoFuncaoObjetiva);
        double custoAtual = solucaoObjetiva2[0];
        qtdAcessoFuncaoObjetiva = solucaoObjetiva2[1];
        double temperatura = temperaturaInicial;

        String execucao = "";
        String retornoGeracaoMelhorAptidao = "";
        String retornoDesenvolvedores = "";
        String retornoFeaturesUtilizadas = "";

        int contadorSprint = 0;
        while(contadorSprint < qtdeSprints) {
            for (int i = 0; i < numeroIteracoes; i++) {
                Release vizinho = gerarVizinho(solucaoOtima);
                int[] solucaoObjetiva3 = Release.funcaoObjetivo(vizinho.getFeatures(), this.funcionalidadeList,  qtdAcessoFuncaoObjetiva);
                double custoVizinho = solucaoObjetiva3[0];
                qtdAcessoFuncaoObjetiva = solucaoObjetiva3[1];
                double variacaoCusto = custoVizinho - custoAtual;

                if (variacaoCusto < 0 || Math.exp(variacaoCusto / temperatura) > Math.random()) {
                    solucaoOtima = vizinho;
                    custoAtual = custoVizinho;
                }
                temperatura *= taxaResfriamento;
                execucao += "\nTemperatura: " + String.valueOf(temperatura)
                        + " - Custo: " + String.valueOf(custoAtual)
                        + " - Ponto de função: " + Release.totalPontoFuncao(vizinho.getFeatures(), this.funcionalidadeList);

                funcionalidadeList = Release.setarFeaturesUsadas(vizinho.getFeatures(), funcionalidadeList);
            }
            melhorReleaseSprint = solucaoOtima;

            retornoGeracaoMelhorAptidao += Release.retornoFeaturesUsadasSprint(contadorSprint, funcionalidadeList);

            Release.atribuirFeatures(melhorReleaseSprint, desenvolvedores, funcionalidadeList);
            retornoDesenvolvedores += Release.retornarDesenvolvedoresString(contadorSprint, desenvolvedores);

            retornoFeaturesUtilizadas += Sprint.montarRetornoSprints(melhorReleaseSprint, String.valueOf(contadorSprint + 1), this.funcionalidadeList, provisao, qtdAcessoFuncaoObjetiva) + "\n";

            contadorSprint++;
        }

        retornoLista.add(String.valueOf(temperaturaInicial));
        retornoLista.add(String.valueOf(taxaResfriamento));
        retornoLista.add(String.valueOf(numeroIteracoes));
        retornoLista.add(String.valueOf(limiteRecursos));
        retornoLista.add(Arrays.toString(solucaoOtima.getFeatures()));
        retornoLista.add(execucao);
        retornoLista.add(String.valueOf(tamanhoSolucao));
        retornoLista.add(String.valueOf(provisao));
        retornoLista.add(String.valueOf(Release.funcaoObjetivo(solucaoOtima.getFeatures(), funcionalidadeList,  qtdAcessoFuncaoObjetiva)[0]));
        retornoLista.add(retornoDesenvolvedores);
        retornoLista.add(retornoGeracaoMelhorAptidao);
        retornoLista.add(retornoFeaturesUtilizadas);
        retornoLista.add(Arrays.toString(qtdeFeaturesPorPrioridade));
        retornoLista.add(String.valueOf(qtdAcessoFuncaoObjetiva));
        retornoLista.add(GraphUtil.gerarGraficoGantt(retornoFeaturesUtilizadas));
        retornoLista.add(GraphUtil.gerarGraficoBurndown(retornoFeaturesUtilizadas, funcionalidadeList));
        retornoLista.add(Sprint.featureSelecionadaProximaSprint(melhorReleaseSprint, funcionalidadeList));


        return retornoLista;
    }

    public Release executarSimulatedAnnealing(Release solucaoOtima, double custoAtual, double temperatura, String execucao){

        return solucaoOtima;
    }

    // Gere uma solução vizinha modificando aleatoriamente a solução atual
    public Release gerarVizinho(Release solucao) {
        Release vizinho = solucao.clone();
        Random rand1 = new Random();
        Random rand2 = new Random();
        Random rand3 = new Random();

        int posicao1 = rand1.nextInt(solucao.getFeatures().length);
        int posicao2 = rand2.nextInt(solucao.getFeatures().length);
        int posicao3 = rand3.nextInt(solucao.getFeatures().length);

        while(posicao1 == posicao2 || posicao2 == posicao3 || posicao3 == posicao1){
            if(posicao1 == posicao2){
                posicao2 = rand2.nextInt(solucao.getFeatures().length);
            } else if(posicao2 == posicao3){
                posicao3 = rand3.nextInt(solucao.getFeatures().length);
            } else if(posicao3 == posicao1){
                posicao1 = rand1.nextInt(solucao.getFeatures().length);
            }
        }

        if(!funcionalidadeList.get(posicao1).isUsada()){
            vizinho.getFeatures()[posicao1] = 1 - vizinho.getFeatures()[posicao1]; // Inverte o valor na posição selecionada
            vizinho.setFeatures(Release.calcularRestricao(provisao, vizinho.getFeatures(), funcionalidadeList));
        }
        if(!funcionalidadeList.get(posicao2).isUsada()){
            vizinho.getFeatures()[posicao2] = 1 - vizinho.getFeatures()[posicao2];
            vizinho.setFeatures(Release.calcularRestricao(provisao, vizinho.getFeatures(), funcionalidadeList));
        }
        if(!funcionalidadeList.get(posicao3).isUsada()){
            vizinho.getFeatures()[posicao3] = 1 - vizinho.getFeatures()[posicao3];
            vizinho.setFeatures(Release.calcularRestricao(provisao, vizinho.getFeatures(), funcionalidadeList));
        }
        return vizinho;
    }

    //Inicializa os dados para a execução do algorítmo
    private void limparDados(){
        funcionalidadeList = new ArrayList<Funcionalidade>();
        desenvolvedores = new ArrayList<Desenvolvedor>();
        temperaturaInicial = 0;
        taxaResfriamento = 0;
        numeroIteracoes = 0;
        tamanhoSolucao = 0;
        limiteRecursos = 0;
        qtdeSprints = 0;
        retornoLista = new ArrayList<String>();
        provisao = 0;
        melhorReleaseSprint = null;
        qtdAcessoFuncaoObjetiva = 0;
    }
}

