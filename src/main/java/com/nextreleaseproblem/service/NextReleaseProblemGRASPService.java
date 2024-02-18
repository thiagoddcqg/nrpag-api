package com.nextreleaseproblem.service;

import com.nextreleaseproblem.model.*;
import com.nextreleaseproblem.util.GraphUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class NextReleaseProblemGRASPService {
    private List<Funcionalidade> funcionalidadeList;
    private int interacoes = 0;
    private int qtdAcessoFuncaoObjetiva = 0;
    private List<Desenvolvedor> desenvolvedores;
    private int provisao = 0;
    private int qtdeSprints = 0;
    private double alfa = 0.0;
    private int tamPopulacao = 0;
    private Random random;
    private Release melhorReleaseSprint = null;

    public List<String> iniciar(List<Funcionalidade> funcionalidadeList, int interacoesinput, double alfainput, int duracaoSprint, int tampopulacaoinput) {
        List<String> retornoLista = new ArrayList<>();

        tamPopulacao = tampopulacaoinput;
        interacoes = interacoesinput;
        alfa = alfainput; // Parâmetro de aleatoriedade
        this.funcionalidadeList = Release.gerarFeatures(funcionalidadeList);
        int[] qtdeFeaturesPorPrioridade = Release.qtdeFeatures(this.funcionalidadeList);
        desenvolvedores = Release.gerarDesenvolvedores(this.funcionalidadeList);
        provisao = Release.calcularProvisao(desenvolvedores, duracaoSprint);
        qtdeSprints = Sprint.calcularQtdeSprints(provisao, funcionalidadeList);

        Release release = grasp(funcionalidadeList, interacoes);

        String execucao = "";
        String retornoGeracaoMelhorAptidao = "";
        String retornoDesenvolvedores = "";
        String retornoFeaturesUtilizadas = "";

        System.out.println("Selected features:");

        int contadorSprint = 0;
        while (contadorSprint < qtdeSprints) {
            for (int featureIndex : release.getFeatures()) {
                System.out.println(funcionalidadeList.get(featureIndex).getId());
            }

            melhorReleaseSprint = grasp(funcionalidadeList, interacoes);

            retornoGeracaoMelhorAptidao += Release.retornoFeaturesUsadasSprint(contadorSprint, funcionalidadeList);
            retornoDesenvolvedores += Release.retornarDesenvolvedoresString(contadorSprint, desenvolvedores);
            retornoFeaturesUtilizadas += Sprint.montarRetornoSprints(melhorReleaseSprint, String.valueOf(contadorSprint + 1), this.funcionalidadeList, provisao, qtdAcessoFuncaoObjetiva) + "\n";

            // retornoLista.add(Sprint.featureSelecionadaProximaSprint(melhorReleaseSprint, featureList));

            contadorSprint++;
        }

        System.out.println("Total score: " + release.getValor());

        retornoLista.add(String.valueOf(release.getValor()));
        retornoLista.add(retornoGeracaoMelhorAptidao);
        retornoLista.add(retornoDesenvolvedores);
        retornoLista.add(retornoFeaturesUtilizadas);
        retornoLista.add(Sprint.featureSelecionadaProximaSprint(melhorReleaseSprint, funcionalidadeList));
        retornoLista.add(Arrays.toString(qtdeFeaturesPorPrioridade));
        retornoLista.add(String.valueOf(provisao));
        retornoLista.add(GraphUtil.gerarGraficoGantt(retornoFeaturesUtilizadas));
        retornoLista.add(GraphUtil.gerarGraficoBurndown(retornoFeaturesUtilizadas, funcionalidadeList));
        retornoLista.add(String.valueOf(Release.somatorioPontosAptidaoProductBacklog(funcionalidadeList)));
        retornoLista.add(Arrays.toString(qtdeFeaturesPorPrioridade));
        retornoLista.add(String.valueOf(melhorReleaseSprint.getValor()));
        retornoLista.add(String.valueOf(qtdAcessoFuncaoObjetiva));

        return retornoLista;
    }

    public Release grasp(List<Funcionalidade> funcionalidades, int provisao) {
        int[] solucaoInicial = Release.inicializar(this.provisao, this.funcionalidadeList);
        int[] funcaoObjetiva = Release.funcaoObjetivo(solucaoInicial, this.funcionalidadeList, qtdAcessoFuncaoObjetiva);
        qtdAcessoFuncaoObjetiva = funcaoObjetiva[1];
        Release melhorRelease = new Release(solucaoInicial, funcaoObjetiva[0]);

        for (int i = 0; i < interacoes; i++) {
            Release atualRelease = construirReleaseGulosa(funcionalidades, provisao);

            buscaLocal(atualRelease, funcionalidades, provisao); // Pode implementar uma busca local opcional

            //melhorRelease == null ||
            if (atualRelease.getValor() > melhorRelease.getValor()) {
                melhorRelease = (Release) atualRelease.clone();
            }
            System.out.println(melhorRelease.toString());
        }
        return melhorRelease;
    }

    private Release construirReleaseGulosa(List<Funcionalidade> funcionalidades, int budget) {
        // Cria a população inicial
        Populacao populacao = Populacao.criarPopulacao(tamPopulacao, funcionalidades, budget);
        Populacao.readequarPopulacao(populacao, funcionalidades, budget);
        List<Release> candidatos = new ArrayList<>();

        int maxCandidatos = (int) Math.ceil(alfa * funcionalidadeList.size());

        for (int i = 0; i < maxCandidatos; i++) {
            int indiceAleatorio = new Random().nextInt(populacao.getReleases().size());
            candidatos.add(populacao.getReleases().get(indiceAleatorio));
        }

        candidatos.sort((i1, i2) -> Integer.compare(Release.funcaoObjetivo(i2.getFeatures(), funcionalidadeList, qtdAcessoFuncaoObjetiva)[0], Release.funcaoObjetivo(i1.getFeatures(), funcionalidadeList, qtdAcessoFuncaoObjetiva)[0]));
        return candidatos.get(0);
    }

    private void buscaLocal(Release release, List<Funcionalidade> funcionalidades, int provisao) {
        Random aleatorio = new Random();

        for (int i = 0; i < interacoes; i++) {
            int indice1 = aleatorio.nextInt(release.getFeatures().length);
            int indice2 = aleatorio.nextInt(release.getFeatures().length);

            // Evitar índices iguais
            while (indice2 == indice1) {
                indice2 = aleatorio.nextInt(release.getFeatures().length);
            }

            int temp = release.getFeatures()[indice1];
            release.getFeatures()[indice1] = release.getFeatures()[indice2];
            release.getFeatures()[indice2] = temp;

            if (release.getValor() > provisao) {
                // Se a troca piorar a solução, reverta
                temp = release.getFeatures()[indice1];
                release.getFeatures()[indice1] = release.getFeatures()[indice2];
                release.getFeatures()[indice2] = temp;
            }
        }
    }
}
