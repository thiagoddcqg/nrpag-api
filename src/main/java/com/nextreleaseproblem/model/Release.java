package com.nextreleaseproblem.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;
import java.util.*;

@Data
@Getter
@Setter
public class Release implements Cloneable {
    // Classe que representa uma versão de lançamento
    private int[] features; // Features/genes da versão
    private static final Random aleatorio = new Random();
    private int valor; // Custo da versão (objetivo a ser minimizado)

    public Release(List<Funcionalidade> funcionalidadeList) {
        features = new int[funcionalidadeList.size()];
    }

    public Release(int[] features, int valor) {
        this.features = features;
        this.valor = valor;
    }

    public static int[] inicializar(int provisao, List<Funcionalidade> funcionalidadeList) {
        int tamanho = funcionalidadeList.size();
        int[] features =  new int[tamanho];
        for(int i = 0; i < funcionalidadeList.size(); i++ ){
            if(funcionalidadeList.get(i).isUsada()){
                features[i] = 0;
            }else {
                features[i] = 1;
            }
        }
        features = calcularRestricao(provisao, features, funcionalidadeList);
        return features;
    }

    public void mutar(double taxaMutacao, List<Funcionalidade> funcionalidadeList, int provisao) {
        for (int i = 0; i < this.features.length; i++) {
            if (Math.random() < taxaMutacao) {
                if(this.features[i] == 0 && !funcionalidadeList.get(i).isUsada()){
                    this.features[i] = 1;
                } else {
                    this.features[i] = 0;
                }
            }
        }
        this.features = Release.calcularRestricao(provisao, this.features, funcionalidadeList);
    }

    @Override
    public Release clone() {
        try {
            Release clonedRelease = (Release) super.clone();
            // Clone o array para evitar cópia superficial
            clonedRelease.features = this.features.clone();
            return clonedRelease;
        } catch (CloneNotSupportedException e) {
            // Isso não deveria acontecer já que implementamos Cloneable
            throw new AssertionError();
        }
    }


    //TODO
    public static int calcularProvisao(List<Desenvolvedor> desenvolvedores, int quantidadeSprint){
        int provisao = 0;
        for(Desenvolvedor desenvolvedor : desenvolvedores){
            provisao += desenvolvedor.getProdutividade();
        }
        return provisao * quantidadeSprint; //total(disponibilidade em semana * fator de eficiência) * (quandidade de semanas)
    }

    public static int[] calcularRestricao(int provisao, int[] features, List<Funcionalidade> funcionalidadeList) {
        while (!isValido(provisao, features, funcionalidadeList)){
            features = reparar(features);
        }
        return features;
    }

    public static int[] reparar(int[] features){
        List<Integer> posicoesUsadas = new ArrayList<Integer>();

        for (int i = 0; i < features.length; i++) {
            if (features[i] == 1) {
                posicoesUsadas.add(i);
            }
        }

        if(posicoesUsadas.size() > 0){
            int posicaoAleatoria = posicoesUsadas.get(aleatorio.nextInt(posicoesUsadas.size())).intValue();
            features[posicaoAleatoria] = 0;
        }

        return features;
    }

    public static boolean isValido(int provisao, int[] features, List<Funcionalidade> funcionalidadeList){
        double soma = totalPontoFuncaoTempo(features, funcionalidadeList);
        return (soma <= provisao);
    }

    public static int totalPontoFuncao(int[] features, List<Funcionalidade> funcionalidadeList){
        int cont = 0;
        for (int i = 0; i < funcionalidadeList.size(); i++) {
            if (features[i] == 1) {
                cont += funcionalidadeList.get(i).getPontoFuncao();
            }
        }
        return cont;
    }

    public static double totalPontoFuncaoTempo(int[] features, List<Funcionalidade> funcionalidadeList){
        double cont = 0;
        for (int i = 0; i < funcionalidadeList.size(); i++) {
            if (features[i] == 1) {
                switch (funcionalidadeList.get(i).getPontoFuncao()){
                    case 1:
                        cont += 0.1;
                        break;
                    case 2:
                        cont += 0.2;
                        break;
                    case 3:
                        cont += 0.6;
                        break;
                    case 5:
                        cont += 1;
                        break;
                    case 8:
                        cont += 1.4;
                        break;
                    case 13:
                        cont += 2;
                        break;
                }
            }
        }
        return cont == 0.0? 1 : cont;
    }

    public static int[] funcaoObjetivo(int[] features, List<Funcionalidade> funcionalidadeList, int qtdAcessoFuncaoObjetiva){
        int[] retorno = new int[2];
        int somaAptidao = 0;
        for (int i = 0; i < funcionalidadeList.size(); i++) {
            if (features[i] == 1) {
                somaAptidao += funcionalidadeList.get(i).getPontoFuncao() * funcionalidadeList.get(i).getPrioridade().getScore();
            }
        }
        retorno[0] = somaAptidao;
        retorno[1] = qtdAcessoFuncaoObjetiva + 1;
        
        return retorno;
    }

    // Identifica as features que foi utilizada na sprint atual
    public static List<Funcionalidade> setarFeaturesUsadas(int[] features, List<Funcionalidade> funcionalidadeList){
        for(int i = 0; i < features.length; i++){
            if(features[i] == 1){
                funcionalidadeList.get(i).setUsada(true);
            }
        }
        return funcionalidadeList;
    }

    // Método auxiliar para converter um array em uma string para impressão
    public static String featureToString(int[] feature) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < feature.length; i++) {
            sb.append(feature[i]);
            if (i < feature.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    //Faz o somatório da aptidão de todas a features
    public static int somatorioPontosAptidaoProductBacklog(List<Funcionalidade> funcionalidadeList) {
        int somatorio = 0;
        for(Funcionalidade funcionalidade : funcionalidadeList){
            somatorio += funcionalidade.getPontoFuncao() * funcionalidade.getPrioridade().getScore();
        }
        return somatorio;
    }

    //Adiciona o ponto de função e prioridade.
    public static List<Funcionalidade> gerarFeatures(List<Funcionalidade> funcionalidadeList){

        List<Integer> pontoFuncaoList = new ArrayList<Integer>();
        pontoFuncaoList.add(1);
        pontoFuncaoList.add(2);
        pontoFuncaoList.add(3);
        pontoFuncaoList.add(5);
        pontoFuncaoList.add(8);
        pontoFuncaoList.add(13);

        Double interacao = 1.0 / funcionalidadeList.size();
        Double porcentagem = interacao;
        int contPontoFuncao = 0;
        for(Funcionalidade funcionalidade : funcionalidadeList){
            if(porcentagem <= 0.02){
                funcionalidade.setPrioridade(NivelPrioridade.URGENTE);
            }else if(porcentagem <= 0.05){
                funcionalidade.setPrioridade(NivelPrioridade.ALTA);
            }else if(porcentagem <= 0.97){
                funcionalidade.setPrioridade(NivelPrioridade.MEDIA);
            }else{
                funcionalidade.setPrioridade(NivelPrioridade.BAIXA);
            }
            funcionalidade.setPontoFuncao(pontoFuncaoList.get(contPontoFuncao));
            porcentagem += interacao;
            contPontoFuncao++;
            if (contPontoFuncao >= pontoFuncaoList.size()){
                contPontoFuncao = 0;
            }
        }
        return funcionalidadeList;
    }

    public static List<Funcionalidade> gerarFeatures(int qtdFeatures, String porcentagemPorNivel, String desnametextarea){
        List<Funcionalidade> funcionalidadeList = new ArrayList<Funcionalidade>();
        return funcionalidadeList;
    }

    //TODO
    public static List<Desenvolvedor> gerarDesenvolvedores(List<Funcionalidade> funcionalidadeList){
        List<Desenvolvedor> desenvolvedores = new ArrayList<Desenvolvedor>();

        HashSet<String> nomeDesenvolvedores = new HashSet<String>();

        List<Integer> disponibilidadeList = new ArrayList<Integer>();
        disponibilidadeList.add(20);
        disponibilidadeList.add(30);
        disponibilidadeList.add(40);

        List<NivelDesenvolvedor> nivelDesenvolvedorList = new ArrayList<NivelDesenvolvedor>();
        nivelDesenvolvedorList.add(NivelDesenvolvedor.JUNIOR);
        nivelDesenvolvedorList.add(NivelDesenvolvedor.PLENO);
        nivelDesenvolvedorList.add(NivelDesenvolvedor.SENIOR);
        nivelDesenvolvedorList.add(NivelDesenvolvedor.PLENO);

        for(Funcionalidade funcionalidade : funcionalidadeList){
            if(!funcionalidade.getAtribuidoPara().isEmpty()){
                nomeDesenvolvedores.add(funcionalidade.getAtribuidoPara());
            }
        }

        int contNivel = 0;
        int contDisponibilidade = 0;
        for(String nome : nomeDesenvolvedores){
            Desenvolvedor desenvolvedor = new Desenvolvedor();
            desenvolvedor.setNome(nome);
            desenvolvedor.setNivelDesenvolvedor(nivelDesenvolvedorList.get(contNivel));
            desenvolvedor.setDisponibilidadeSemanal(disponibilidadeList.get(contDisponibilidade));
            desenvolvedores.add(desenvolvedor);
            contNivel++;
            contDisponibilidade++;
            if(contNivel >= nivelDesenvolvedorList.size()){
                contNivel = 0;
            }
            if(contDisponibilidade >= disponibilidadeList.size()){
                contDisponibilidade = 0;
            }
        }
        return desenvolvedores;
    }

    //Gera os desenvolvedores com features aleatórias
    public void gerarDesenvolvedoresAleatorios(List<Funcionalidade> funcionalidadeList){
        Random aleatorio = new Random();

        List<Desenvolvedor> desenvolvedores = new ArrayList<Desenvolvedor>();

        HashSet<String> nomeDesenvolvedores = new HashSet<String>();

        List<Integer> disponibilidadeList = new ArrayList<Integer>();
        disponibilidadeList.add(20);
        disponibilidadeList.add(30);
        disponibilidadeList.add(40);

        List<NivelDesenvolvedor> nivelDesenvolvedorList = new ArrayList<NivelDesenvolvedor>();
        nivelDesenvolvedorList.add(NivelDesenvolvedor.JUNIOR);
        nivelDesenvolvedorList.add(NivelDesenvolvedor.PLENO);
        nivelDesenvolvedorList.add(NivelDesenvolvedor.SENIOR);

        for(Funcionalidade funcionalidade : funcionalidadeList){
            if(!funcionalidade.getAtribuidoPara().isEmpty()){
                nomeDesenvolvedores.add(funcionalidade.getAtribuidoPara());
            }
        }

        for(String nome : nomeDesenvolvedores){
            Desenvolvedor desenvolvedor = new Desenvolvedor();
            desenvolvedor.setNome(nome);
            desenvolvedor.setNivelDesenvolvedor(nivelDesenvolvedorList.get(aleatorio.nextInt(3)));
            desenvolvedor.setDisponibilidadeSemanal(disponibilidadeList.get(aleatorio.nextInt(3)));
            desenvolvedores.add(desenvolvedor);
        }
    }

    @Override
    public String toString() {
        return "Release{" +
                "features=" + Arrays.toString(features) +
                ", valor=" + valor +
                '}';
    }

    // Gera as releases com features aleatórias
    public void gerarReleasesAleatorias(List<Funcionalidade> funcionalidadeList){
        Random aleatorio = new Random();

        funcionalidadeList = new ArrayList<Funcionalidade>();

        List<Integer> pontoFuncaoList = new ArrayList<Integer>();
        pontoFuncaoList.add(1);
        pontoFuncaoList.add(2);
        pontoFuncaoList.add(3);
        pontoFuncaoList.add(5);
        pontoFuncaoList.add(8);
        pontoFuncaoList.add(13);

        Double porcentagem = 0.0;
        for(Funcionalidade funcionalidade : funcionalidadeList){
            porcentagem = aleatorio.nextDouble();
            if(porcentagem <= 0.02){
                funcionalidade.setPrioridade(NivelPrioridade.URGENTE);
            }else if(porcentagem <= 0.05){
                funcionalidade.setPrioridade(NivelPrioridade.ALTA);
            }else if(porcentagem <= 0.97){
                funcionalidade.setPrioridade(NivelPrioridade.MEDIA);
            }else if(porcentagem <= 1.0){
                funcionalidade.setPrioridade(NivelPrioridade.BAIXA);
            }
            funcionalidade.setPontoFuncao(pontoFuncaoList.get(aleatorio.nextInt(6)));
        }

        for(int i = 0; i < funcionalidadeList.size(); i++){
            funcionalidadeList.add(funcionalidadeList.get(i));
        }
    }

    //Monta os dados a ser exibidos no textarea Features
    public static String retornoFeaturesUsadasSprint(int contadorSprint, List<Funcionalidade> funcionalidadeList){
        int cont1 = 1;
        String retorno = "\n################## - Sprint: " + (contadorSprint + 1) + " - ##################" + "\n";
        for (int i = 0; i < funcionalidadeList.size(); i++) {
            if(!funcionalidadeList.get(i).isUsada()){
                retorno += cont1 + " - Feature #" + funcionalidadeList.get(i).getId()
                        + " - Ponto de função: " + funcionalidadeList.get(i).getPontoFuncao() + " - Prioridade: "
                        + funcionalidadeList.get(i).getPrioridade().toString() + "\n";
                cont1++;
            }
        }
        return retorno;
    }

    //Monta os dados a serem exibidos no textarea Implementadores
    public static String retornarDesenvolvedoresString(int contadorSprint, List<Desenvolvedor> desenvolvedores) {
        String retorno = "\n################## - Sprint: " + (contadorSprint + 1) + " - ##################" + "\n";
        for(Desenvolvedor desenvolvedor : desenvolvedores){
            retorno += desenvolvedor.toString() + "\n";
            desenvolvedor.limpar();
        }
        return retorno;
    }


    //Atribui as features aos desenvolvedores
    public static void atribuirFeatures(Release release, List<Desenvolvedor> desenvolvedores, List<Funcionalidade> funcionalidadeList){
        int cont = 0;
        for(Desenvolvedor desenvolvedor : desenvolvedores){
            desenvolvedor.setFuncionalidadeList(new ArrayList<Funcionalidade>());
            int pontoFuncaoTotal = desenvolvedor.getProdutividade();
            int somaPontoFuncao = 0;
            while (cont < release.getFeatures().length){
                if(release.getFeatures()[cont] == 1){
                    if((somaPontoFuncao + funcionalidadeList.get(cont).getPontoFuncao()) > pontoFuncaoTotal){
                        break;
                    }else {

                        somaPontoFuncao += funcionalidadeList.get(cont).getPontoFuncao();
                        desenvolvedor.getFuncionalidadeList().add(funcionalidadeList.get(cont));
                    }
                }
                cont++;
            }
        }
    }

    //Gera dados para mostrar na tela de todas as features e benefícios
    public static void gerarDadosFeaturesBeneficios(String[] featuresString, int[] beneficiosString, List<Funcionalidade> funcionalidadeList) {
        for (int i = 0; i < funcionalidadeList.size(); i++) {
            featuresString[i] = funcionalidadeList.get(i).getId();
            beneficiosString[i] = funcionalidadeList.get(i).getPontoFuncao();
        }
    }

    //Calcula a taxa de aproveitamento da provisão
    public static String calcularTaxaAproveitamentoProvisao(int somatorioPontosFuncao, int provisao) {
        DecimalFormat df = new DecimalFormat("#.00");

        somatorioPontosFuncao = (somatorioPontosFuncao == 0.0)? 1 : somatorioPontosFuncao;

        if (provisao == 0 || somatorioPontosFuncao == 0) {
            throw new IllegalArgumentException("Os valores não podem ser iguais a zero");
        }

        double proporcao = (double) somatorioPontosFuncao / provisao;
        /* proporcao = converter(proporcao);*/

        return df.format(proporcao * 100)+"%";
    }

    //Contabiliza as features por nível de prioridade
    public static int[] qtdeFeatures(List<Funcionalidade> funcionalidadeList){
        List<Funcionalidade> prioridadeUrgente = new ArrayList<>();
        List<Funcionalidade> prioridadeAlta = new ArrayList<>();
        List<Funcionalidade> prioridadeMedia = new ArrayList<>();
        List<Funcionalidade> prioridadeBaixa = new ArrayList<>();

        for(Funcionalidade funcionalidade : funcionalidadeList){
            if(funcionalidade.getPrioridade().equals(NivelPrioridade.URGENTE)){
                prioridadeUrgente.add(funcionalidade);
            }else if(funcionalidade.getPrioridade().equals(NivelPrioridade.ALTA)){
                prioridadeAlta.add(funcionalidade);
            }else if(funcionalidade.getPrioridade().equals(NivelPrioridade.MEDIA)){
                prioridadeMedia.add(funcionalidade);
            }else if(funcionalidade.getPrioridade().equals(NivelPrioridade.BAIXA)){
                prioridadeBaixa.add(funcionalidade);
            }
        }

        int[] qtdeFeaturesPorPrioridade = {prioridadeUrgente.size(),
                prioridadeAlta.size(),
                prioridadeMedia.size(),
                prioridadeBaixa.size()};
        return qtdeFeaturesPorPrioridade;
    }


    //Retorna o melhor indivíduo de uma população
    public static Release getMelhorRelease(Populacao populacao, List<Funcionalidade> funcionalidadeList, int qtdAcessoFuncaoObjetiva){
        populacao.getReleases().sort((i1, i2) -> Integer.compare(Release.funcaoObjetivo(i2.getFeatures(), funcionalidadeList, qtdAcessoFuncaoObjetiva)[0], Release.funcaoObjetivo(i1.getFeatures(), funcionalidadeList, qtdAcessoFuncaoObjetiva)[0]));
        return populacao.getReleases().get(0);
    }

    //Faz o somatório do ponto de função de todas as features
    public static int somatorioPontosFuncao(List<Funcionalidade> funcionalidadeList) {
        int soma = 0;
        for(Funcionalidade funcionalidade : funcionalidadeList){
            soma += funcionalidade.getPontoFuncao();
        }
        return soma;
    }

    private int calculateValor() {
        int totalScore = 0;
        for (int featureIndex : features) {
            totalScore += Funcionalidade.getNivelPrioridade(featureIndex).getScore();
        }
        return totalScore;
    }

    public Release(int[] features) {
        this.features = Arrays.copyOf(features, features.length);
        this.valor = calculateValor();
    }


}