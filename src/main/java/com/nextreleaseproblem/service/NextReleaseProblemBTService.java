package com.nextreleaseproblem.service;

import com.nextreleaseproblem.model.*;
import com.nextreleaseproblem.util.GraphUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class NextReleaseProblemBTService {
    int interacoes = 0;
    int tamanhoTabu = 0;
    private int tamanhoSolucao = 0;
    private List<Desenvolvedor> desenvolvedores;
    private int provisao = 0;
    private int qtdeSprints = 0;
    private int contFuncaoObjetivo = 0;
    private Release melhorReleaseSprint = null;
    private List<Funcionalidade> funcionalidadeList;

    private Release melhorReleasePrimeira = null;

    private int qtdAcessoFuncaoObjetiva = 0;

    public List<String> iniciar(List<Funcionalidade> funcionalidadeList, int interacoes, int tamanhoTabu, int duracaoSprint) {
        // Limpa as variáveis globais antes de iniciar a execução.
        limparDados();

        List<String> retornoLista = new ArrayList<>();

        tamanhoSolucao = funcionalidadeList.size();
        this.funcionalidadeList = Release.gerarFeatures(funcionalidadeList);
        desenvolvedores = Release.gerarDesenvolvedores(this.funcionalidadeList);
        provisao = Release.calcularProvisao(desenvolvedores, duracaoSprint);

        int[] qtdeFeaturesPorPrioridade = Release.qtdeFeatures(this.funcionalidadeList);
        desenvolvedores = Release.gerarDesenvolvedores(this.funcionalidadeList);
        provisao = Release.calcularProvisao(desenvolvedores, duracaoSprint);
        qtdeSprints = Sprint.calcularQtdeSprints(provisao, funcionalidadeList);

        // Gera dados de retorno para a tela de Features e Benefícios.
        String[] featuresString = new String[this.funcionalidadeList.size()];
        int[] beneficiosString = new int[this.funcionalidadeList.size()];
        Release.gerarDadosFeaturesBeneficios(featuresString, beneficiosString, funcionalidadeList);

        // Solução inicial da release
        int[] solucaoInicial = Release.inicializar(provisao, this.funcionalidadeList);

        int[] funcaoObjetiva = Release.funcaoObjetivo(solucaoInicial, this.funcionalidadeList, qtdAcessoFuncaoObjetiva);

        Release releaseInicial = new Release(solucaoInicial, funcaoObjetiva[0]);
        qtdAcessoFuncaoObjetiva = funcaoObjetiva[1];

        String retornoGeracaoMelhorAptidao = "";
        String retornoDesenvolvedores = "";
        int totalSprints = 0;

        String retornoFeaturesUtilizadas = "";

        int contadorSprint = 0;

        while(contadorSprint < qtdeSprints) {
            melhorReleaseSprint = executarBuscaTabu(releaseInicial, interacoes, tamanhoTabu);
            if(contadorSprint == 0)
                melhorReleasePrimeira = melhorReleaseSprint;

            System.out.println("\nSprint: " + (contadorSprint + 1) + " - Melhor Individuo, Aptidão: " + Release.funcaoObjetivo(this.melhorReleaseSprint.getFeatures(), this.funcionalidadeList, qtdAcessoFuncaoObjetiva)[0]);
            System.out.println("Features selecionadas:");

            retornoGeracaoMelhorAptidao += Release.retornoFeaturesUsadasSprint(contadorSprint, funcionalidadeList);

            Release.atribuirFeatures(melhorReleaseSprint, desenvolvedores, funcionalidadeList);
            retornoDesenvolvedores += Release.retornarDesenvolvedoresString(contadorSprint, desenvolvedores);

            retornoFeaturesUtilizadas += Sprint.montarRetornoSprints(melhorReleaseSprint, String.valueOf(contadorSprint + 1), this.funcionalidadeList, provisao, qtdAcessoFuncaoObjetiva) + "\n";

            totalSprints = Sprint.calcularTotalUsadas(funcionalidadeList);
            System.out.println("Features consumidas: " + totalSprints);

            this.funcionalidadeList = Release.setarFeaturesUsadas(melhorReleaseSprint.getFeatures(), this.funcionalidadeList);

            contadorSprint++;
        }

        retornoFeaturesUtilizadas += "Total: " + this.funcionalidadeList.size();

        retornoLista.add(String.valueOf(interacoes));
        retornoLista.add(String.valueOf(tamanhoTabu));
        retornoLista.add(Release.featureToString(melhorReleaseSprint.getFeatures()));
        retornoLista.add(String.valueOf(melhorReleasePrimeira.getValor()));
        retornoLista.add(retornoDesenvolvedores);
        retornoLista.add(retornoGeracaoMelhorAptidao);
        retornoLista.add(retornoFeaturesUtilizadas);
        retornoLista.add(Arrays.toString(qtdeFeaturesPorPrioridade));
        retornoLista.add(String.valueOf(provisao));
        retornoLista.add(String.valueOf(funcionalidadeList.size()));
        retornoLista.add(String.valueOf(qtdAcessoFuncaoObjetiva));
        retornoLista.add(GraphUtil.gerarGraficoGantt(retornoFeaturesUtilizadas));
        retornoLista.add(GraphUtil.gerarGraficoBurndown(retornoFeaturesUtilizadas, funcionalidadeList));
        retornoLista.add(Sprint.featureSelecionadaProximaSprint(melhorReleaseSprint, funcionalidadeList));

        return retornoLista;
    }

    public Release executarBuscaTabu(Release solucaoInicial, int iterations, int tamTabu) {
        Release solucaoAtual = solucaoInicial;
        Release melhorSolucao = solucaoInicial.clone();

        List<Release> listaTabu = new ArrayList<>();

        for (int i = 0; i < iterations; i++) {
            List<Release> vizinhos = gerarVizinhos(solucaoAtual);

            // Encontrar o melhor vizinho que não está na lista tabu
            Release melhorVizinho = acharMelhorVizinhoNaoTabu(vizinhos, listaTabu);

            // Atualizar a solução atual
            solucaoAtual = melhorVizinho;

            // Atualizar a melhor solução global
            if (solucaoAtual.getValor() > melhorSolucao.getValor()) {
                melhorSolucao = solucaoAtual.clone();
            }

            // Adicionar o movimento à lista tabu
            listaTabu.add(solucaoAtual);
            if (listaTabu.size() > tamTabu) {
                listaTabu.remove(0);  // Remover o mais antigo se a lista atingir o tamanho máximo
            }
        }
        return melhorSolucao;
    }

    private List<Release> gerarVizinhos(Release release) {
        // Gera vizinhos alterando uma feature da versão atual
        List<Release> vizinhos = new ArrayList<>();
        for (int i = 0; i < release.getFeatures().length; i++) {
            int[] featuresVizinho = release.getFeatures().clone();
            featuresVizinho[i] = 1 - featuresVizinho[i]; // Inverte a feature
            featuresVizinho = Release.calcularRestricao(provisao, featuresVizinho, funcionalidadeList);
            int[] funcaoObjetiva = Release.funcaoObjetivo(featuresVizinho, this.funcionalidadeList, qtdAcessoFuncaoObjetiva);
            int valorVizinho = funcaoObjetiva[0];
            qtdAcessoFuncaoObjetiva = funcaoObjetiva[1];
            vizinhos.add(new Release(featuresVizinho, valorVizinho));
        }
        return vizinhos;
    }

    private Release acharMelhorVizinhoNaoTabu(List<Release> vizinhos, List<Release> listaTabu) {
        // Encontre o melhor vizinho que não está na lista tabu
        Release melhorVizinho = null;
        int melhorValor = Integer.MAX_VALUE;

        for (Release vizinho : vizinhos) {
            if (!listaTabu.contains(vizinho) && vizinho.getValor() < melhorValor) {
                melhorVizinho = vizinho;
                melhorValor = vizinho.getValor();
            }
        }
        return melhorVizinho;
    }

    // Inicializa os dados para a execução do algorítmo
    private void limparDados(){
        interacoes = 0;
        tamanhoTabu = 0;
        funcionalidadeList = new ArrayList<Funcionalidade>();
        desenvolvedores = new ArrayList<Desenvolvedor>();
        provisao = 0;
        qtdeSprints = 0;
        contFuncaoObjetivo = 0;
        melhorReleaseSprint = null;
        qtdAcessoFuncaoObjetiva = 0;
    }
}
