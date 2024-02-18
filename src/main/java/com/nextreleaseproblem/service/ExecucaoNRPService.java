package com.nextreleaseproblem.service;

import com.nextreleaseproblem.model.ResultadoNRP;
import com.nextreleaseproblem.model.parametros.AlgoritmoEnum;
import com.nextreleaseproblem.repository.AlgoritmoExecucaoRepository;
import com.nextreleaseproblem.repository.entity.AlgoritmoExecucao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class ExecucaoNRPService {

    private final AlgoritmoExecucaoRepository algoritmoExecucaoRepository;

    public List<ResultadoNRP> obterResultadoNRP(){

        //Monta os dados
        List<AlgoritmoExecucao> algoritmoExecucaoList = algoritmoExecucaoRepository.findAll();

        //Prepara os dados iniciais
        List<ResultadoNRP> resultadoNRPList = new ArrayList<>();
        for(AlgoritmoEnum algoritmoEnum : AlgoritmoEnum.values()){
            resultadoNRPList.add(ResultadoNRP.builder().algoritmo(algoritmoEnum.getNome()).build());
        }

        //Cálculo de média
        for(ResultadoNRP resultadoNRP : resultadoNRPList){
            int cont = 0;
            double somatorioMinimo = 0.0;
            double somatorioMaximo = 0.0;
            for(AlgoritmoExecucao algoritmoExecucao : algoritmoExecucaoList){
                if(algoritmoExecucao.getAlgoritmoEnum().getNome().equals(resultadoNRP.getAlgoritmo())){
                    cont++;
                    somatorioMinimo += algoritmoExecucao.getMinimoy();
                    somatorioMaximo += algoritmoExecucao.getMaximoy();
                }
            }
            resultadoNRP.setCont(cont);
            resultadoNRP.setMediaMinimoy(somatorioMinimo / cont);
            resultadoNRP.setMediaMaximoy(somatorioMaximo / cont);

        }

        //Desvio Padrão
        for(ResultadoNRP resultadoNRP : resultadoNRPList){
            int cont = 0;
            double somatorioDiferencaMinimo = 0.0;
            double somatorioDiferencaMaximo = 0.0;
            for(AlgoritmoExecucao algoritmoExecucao : algoritmoExecucaoList){
                if(algoritmoExecucao.getAlgoritmoEnum().getNome().equals(resultadoNRP.getAlgoritmo())){
                    cont++;
                    //Calcular a soma dos quadrados das diferenças
                    double diferencaMinimo = algoritmoExecucao.getMinimoy() - resultadoNRP.getMediaMinimoy();
                    double diferencaMaximo = algoritmoExecucao.getMaximoy() - resultadoNRP.getMediaMaximoy();

                    somatorioDiferencaMinimo += diferencaMinimo * diferencaMinimo;
                    somatorioDiferencaMaximo += diferencaMaximo * diferencaMaximo;
                }

                // Calcular a média dos quadrados das diferenças
                double minimoMediaQuadradosDiferencas = somatorioDiferencaMinimo / (cont - 1);
                double maximoMediaQuadradosDiferencas = somatorioDiferencaMaximo / (cont - 1);

                // Passo 4: Calcular a raiz quadrada da média dos quadrados das diferenças
                resultadoNRP.setDesvioPadraoMinimo(Math.sqrt(minimoMediaQuadradosDiferencas));
                resultadoNRP.setDesvioPadraoMaximo(Math.sqrt(maximoMediaQuadradosDiferencas));
            }
        }

        //Calcular o tempo
        for(ResultadoNRP resultadoNRP : resultadoNRPList){
            Duration total = Duration.ZERO;
            int cont = 0;
            for(AlgoritmoExecucao algoritmoExecucao : algoritmoExecucaoList){
                if(algoritmoExecucao.getAlgoritmoEnum().getNome().equals(resultadoNRP.getAlgoritmo()) && Objects.nonNull(algoritmoExecucao.getTempo())){
                    total = total.plus(algoritmoExecucao.getTempo());
                    cont++;
                }
            }
            double totalDouble = total.getSeconds();
            double totalMedia = totalDouble / cont;
            resultadoNRP.setTempoMedio(totalMedia);
        }

        return resultadoNRPList;
    }

    public double totalTempo(List<ResultadoNRP> resultadoNRPList){
        double total = 0.0;
        for(ResultadoNRP resultadoNRP : resultadoNRPList){
            total += resultadoNRP.getTempoMedio();
        }
        return total;
    }

    public List<ResultadoNRP> calcularProporcao(List<ResultadoNRP> resultadoNRPList, double total){
        for(ResultadoNRP resultadoNRP : resultadoNRPList){
            resultadoNRP.setProporcao((resultadoNRP.getTempoMedio() * 100) / total);
        }
        return resultadoNRPList;
    }

    public double totalProporcao(List<ResultadoNRP> resultadoNRPList){
        double total = 0.0;
        for(ResultadoNRP resultadoNRP : resultadoNRPList){
            total += resultadoNRP.getProporcao();
        }
        return total;
    }

}
