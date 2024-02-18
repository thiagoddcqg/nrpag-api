package com.nextreleaseproblem.service;

import com.nextreleaseproblem.model.*;
import com.nextreleaseproblem.util.GraphUtil;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class NextReleaseProblemAGService {
    private int tamanhoPopulacao = 0;
    private int qtdeGeracoes = 0;
    private double taxaMutacao = 0.0;
    private int tamanhoTorneio = 0;
    private int somatorioPontosFuncao = 0;
    private double chanceCruzamento = 0.0;
    private List<Funcionalidade> funcionalidadeList;
    private List<Desenvolvedor> desenvolvedores;
    private int provisao = 0;
    private int qtdeSprints = 0;
    private int contFuncaoObjetivo = 0;
    private Release melhorRelease;
    private String tiposelecao = "";
    private String tipocruzamento = "";
    private static final Random aleatorio = new Random();

    private int qtdAcessoFuncaoObjetiva = 0;

    //Inicializa o processo do Next Release Problem
    public List<String> iniciar(List<Funcionalidade> funcionalidadeList, String taxamutacaoinput,
                                String populacaoinput, String geracaoinput, String torneioinput, String chancecruzamentoinput,
                                String selecao, String cuzamento, int duracaoSprint) {

        //Limpa as variáveis globais antes de iniciar a execução.
        limparDados();

        taxaMutacao = Double.valueOf(Strings.isBlank(taxamutacaoinput)? "0.1" : taxamutacaoinput);
        tamanhoPopulacao = Integer.parseInt(populacaoinput);
        qtdeGeracoes = Integer.parseInt(geracaoinput);
        tamanhoTorneio = Integer.parseInt(torneioinput);
        chanceCruzamento = Double.valueOf(chancecruzamentoinput) / 100;
        tiposelecao = selecao;
        tipocruzamento = cuzamento;

        List<String> retornoLista = new ArrayList<String>();

        this.funcionalidadeList = Release.gerarFeatures(funcionalidadeList);
        int[] qtdeFeaturesPorPrioridade = Release.qtdeFeatures(this.funcionalidadeList);
        desenvolvedores = Release.gerarDesenvolvedores(this.funcionalidadeList);
        provisao = Release.calcularProvisao(desenvolvedores, duracaoSprint);
        qtdeSprints = Sprint.calcularQtdeSprints(provisao, funcionalidadeList);

        //Gera dados de retorno para a tela de Features e Benefícios.
        String[] featuresString = new String[this.funcionalidadeList.size()];
        int[] beneficiosString = new int[this.funcionalidadeList.size()];
        Release.gerarDadosFeaturesBeneficios(featuresString, beneficiosString, funcionalidadeList);

        //Cria a população inicial
        Populacao populacao = Populacao.criarPopulacao(tamanhoPopulacao, funcionalidadeList, provisao);
        Populacao.readequarPopulacao(populacao, funcionalidadeList, provisao);

        String retornoGeracaoMelhorAptidao = "";
        String retornoDesenvolvedores = "";
        int totalSprints = 0;

        String retornoFeaturesUtilizadas = "";

        int contadorSprint = 0;
        //Faz um loop para cada sprint
        while(contadorSprint < qtdeSprints){

            int geracaoCont = 0;

            //Faz um loop pela quantidade de gerações definidas
            while (geracaoCont < qtdeGeracoes) {

                Populacao novaPopulacao = new Populacao();
                novaPopulacao.getReleases().add(Release.getMelhorRelease(populacao, funcionalidadeList, qtdAcessoFuncaoObjetiva)); // Elitismo

                //Faz um loop para cada indivíduo da população
                while (novaPopulacao.getReleases().size() < tamanhoPopulacao) {
                    Release pai1 = null;
                    Release pai2 = null;
                    if(tiposelecao.equals("roleta")){
                        pai1 = roleta(populacao);
                        pai2 = roleta(populacao);
                    } else if (tiposelecao.equals("torneio")) {
                        pai1 = torneio(populacao);
                        pai2 = torneio(populacao);
                    }
                    Release filho = null;
                    if(tipocruzamento.equals("pontocruzamento")){
                        filho = cruzamentoPontoCruzamento(pai1, pai2, this.funcionalidadeList);
                    } else if (tipocruzamento.equals("mascara")) {
                        filho = cruzamentoMascara(pai1, pai2, this.funcionalidadeList);

                    }
                    filho.mutar(taxaMutacao, this.funcionalidadeList, provisao);
                    novaPopulacao.getReleases().add(filho);
                }
                populacao = novaPopulacao;
                Populacao.readequarPopulacao(populacao, funcionalidadeList, provisao);
                geracaoCont++;
            }

            Release melhorReleaseSprint = Release.getMelhorRelease(populacao, funcionalidadeList, qtdAcessoFuncaoObjetiva);

            if(contadorSprint == 0){
                melhorRelease = melhorReleaseSprint;
            }

            System.out.println("\nSprint: " + (contadorSprint + 1) + " - Melhor Individuo, Aptidão: " + Release.funcaoObjetivo(melhorReleaseSprint.getFeatures(), this.funcionalidadeList, qtdAcessoFuncaoObjetiva));
            System.out.println("Features selecionadas:");

            retornoGeracaoMelhorAptidao += Release.retornoFeaturesUsadasSprint(contadorSprint, funcionalidadeList);

            Release.atribuirFeatures(melhorReleaseSprint, desenvolvedores, funcionalidadeList);
            retornoDesenvolvedores += Release.retornarDesenvolvedoresString(contadorSprint, desenvolvedores);

            retornoFeaturesUtilizadas += Sprint.montarRetornoSprints(melhorReleaseSprint, String.valueOf(contadorSprint + 1), this.funcionalidadeList, provisao, qtdAcessoFuncaoObjetiva) + "\n";

            totalSprints = Sprint.calcularTotalUsadas(funcionalidadeList);
            System.out.println("Features consumidas: " + totalSprints);

            this.funcionalidadeList = Release.setarFeaturesUsadas(melhorReleaseSprint.getFeatures(), this.funcionalidadeList);

            populacao = Populacao.criarPopulacao(tamanhoPopulacao, funcionalidadeList, provisao);
            Populacao.readequarPopulacao(populacao, funcionalidadeList, provisao);
            contadorSprint++;
        }

        retornoFeaturesUtilizadas += "Total: " + this.funcionalidadeList.size();

        // Montar o retorno dos dados para a tela
        retornoLista.add(String.valueOf(tamanhoPopulacao));
        retornoLista.add(String.valueOf(qtdeGeracoes));
        retornoLista.add(String.valueOf(taxaMutacao));
        retornoLista.add(Arrays.toString(featuresString));
        retornoLista.add(Arrays.toString(beneficiosString));
        retornoLista.add(String.valueOf(provisao));
        retornoLista.add(retornoGeracaoMelhorAptidao);
        retornoLista.add(String.valueOf(Release.funcaoObjetivo(melhorRelease.getFeatures(), this.funcionalidadeList, qtdAcessoFuncaoObjetiva)[0]));
        retornoLista.add(Sprint.featureSelecionadaProximaSprint(melhorRelease, funcionalidadeList));
        retornoLista.add(String.valueOf(Release.somatorioPontosFuncao(funcionalidadeList)));
        retornoLista.add(Arrays.toString(qtdeFeaturesPorPrioridade));
        retornoLista.add(retornoDesenvolvedores);
        retornoLista.add(retornoFeaturesUtilizadas);
        retornoLista.add(String.valueOf(tamanhoTorneio));
        retornoLista.add(String.valueOf(contFuncaoObjetivo));
        retornoLista.add(GraphUtil.gerarGraficoGantt(retornoFeaturesUtilizadas));
        retornoLista.add(GraphUtil.gerarGraficoBurndown(retornoFeaturesUtilizadas, funcionalidadeList));
        retornoLista.add((chanceCruzamento * 100) + "%");
        retornoLista.add(String.valueOf(qtdeSprints));
        retornoLista.add(String.valueOf(Release.somatorioPontosAptidaoProductBacklog(funcionalidadeList)));
        retornoLista.add(tiposelecao);
        retornoLista.add(String.valueOf(funcionalidadeList.size()));

        return retornoLista;
    }

    //Inicializa os dados para a execução do algorítmo
    private void limparDados(){
        tamanhoPopulacao = 0;
        qtdeGeracoes = 0;
        taxaMutacao = 0.0;
        tamanhoTorneio = 0;
        somatorioPontosFuncao = 0;
        chanceCruzamento = 0.0;
        funcionalidadeList = new ArrayList<Funcionalidade>();
        desenvolvedores = new ArrayList<Desenvolvedor>();
        provisao = 0;
        qtdeSprints = 0;
        contFuncaoObjetivo = 0;
        melhorRelease = null;
        qtdAcessoFuncaoObjetiva = 0;
    }

    //Realiza a seleção de indivíduo por torneio
    //Não está sendo utilizado pelo algoritmo
    private Release torneio(Populacao populacao) {
        List<Release> torneio = new ArrayList<>();

        for (int i = 0; i < tamanhoTorneio; i++) {
            int indiceAleatorio = new Random().nextInt(populacao.getReleases().size());
            torneio.add(populacao.getReleases().get(indiceAleatorio));
        }

        torneio.sort((i1, i2) -> Integer.compare(Release.funcaoObjetivo(i2.getFeatures(), funcionalidadeList, qtdAcessoFuncaoObjetiva)[0], Release.funcaoObjetivo(i1.getFeatures(), funcionalidadeList, qtdAcessoFuncaoObjetiva)[0]));

        return torneio.get(0);
    }

    //Faz a seleção de indivíduo por roleta
    private Release roleta(Populacao populacao){
        int totalAptidao = populacao.getTotalAptidao(funcionalidadeList, qtdAcessoFuncaoObjetiva);

        // Seleção por roleta
        double valorAleatorio = aleatorio.nextDouble() * totalAptidao;
        double soma = 0;

        for (Release release : populacao.getReleases()) {
            soma += Release.funcaoObjetivo(release.getFeatures(), funcionalidadeList, qtdAcessoFuncaoObjetiva)[0];
            if (soma >= valorAleatorio) {
                return release;
            }
        }

        return null;
    }

    //Cruzamento utilizando troca aleatória de gens
    private Release cruzamentoMascara(Release pai1, Release pai2, List<Funcionalidade> funcionalidadeList) {
        Release descendente = new Release(funcionalidadeList);

        if(aleatorio.nextDouble() <= chanceCruzamento){
            for (int i = 0; i < pai1.getFeatures().length; i++) {
                if (Math.random() < 0.5) {
                    descendente.getFeatures()[i] = pai1.getFeatures()[i];
                } else {
                    descendente.getFeatures()[i] = pai2.getFeatures()[i];
                }
            }
        }else{
            if(aleatorio.nextBoolean()){
                descendente = pai1;
            }else{
                descendente = pai2;
            }
        }
        descendente.setFeatures(Release.calcularRestricao(provisao, descendente.getFeatures(), funcionalidadeList));
        return descendente;
    }

    //Cruzamento utilizando ponto de cruzamento
    private Release cruzamentoPontoCruzamento(Release pai1, Release pai2, List<Funcionalidade> funcionalidadeList) {
        Release descendente = new Release(funcionalidadeList);

        if(aleatorio.nextDouble() <= chanceCruzamento){
            //Seleciona um ponto de cruzamento aleatório
            int pontoCruzamento = aleatorio.nextInt(pai1.getFeatures().length);
            for (int i = 0; i < pai1.getFeatures().length; i++) {
                if(i < pontoCruzamento){
                    descendente.getFeatures()[i] = pai1.getFeatures()[i];
                }else{
                    descendente.getFeatures()[i] = pai2.getFeatures()[i];
                }
            }
        }else{
            if(aleatorio.nextBoolean()){
                descendente = pai1;
            }else{
                descendente = pai2;
            }
        }
        descendente.setFeatures(Release.calcularRestricao(provisao, descendente.getFeatures(), funcionalidadeList));
        return descendente;
    }
}