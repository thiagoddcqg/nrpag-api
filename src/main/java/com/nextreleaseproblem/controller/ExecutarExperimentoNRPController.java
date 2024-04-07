package com.nextreleaseproblem.controller;

import com.nextreleaseproblem.model.AlgoritmoTempo;
import com.nextreleaseproblem.model.GeradorParametrosPadrao;
import com.nextreleaseproblem.model.Retorno;
import com.nextreleaseproblem.model.TipoExperimento;
import com.nextreleaseproblem.model.parametros.ParametrosIteracaoPadrao;
import com.nextreleaseproblem.model.parametros.ParametrosPadrao;
import com.nextreleaseproblem.repository.entity.AlgoritmoExecucao;
import com.nextreleaseproblem.repository.entity.ExecucaoMetaheuristicas;
import com.nextreleaseproblem.service.ExecucaoAlgoritmoService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/algoritmogeneticonrp/executar-experimento-nrp")
public class ExecutarExperimentoNRPController {

    private final ExecucaoAlgoritmoService execucaoAlgoritmoService;
    private String graficoBase64 = "";
    private List<String> graficoBase64List = new ArrayList<>();
    private String execucoes = "";

    @GetMapping("/")
    public String index(Model model) {

        model.addAttribute("maxfuncionarios", ParametrosPadrao.MAXIMO_DE_FUNCIONARIOS);
        model.addAttribute("funcionariosiniciais", ParametrosPadrao.FUNCIONARIOS_INICIAIS);
        model.addAttribute("numerofeatures", ParametrosPadrao.NUMERO_DE_FEATURES);
        model.addAttribute("avaliarhabilidadesfeatures", ParametrosPadrao.AVALIAR_HABILIDADES_POR_FEATURES);
        model.addAttribute("reproducaoteste", ParametrosPadrao.REPRODUCAO_DE_TESTE);
        model.addAttribute("incrementofuncionarios", ParametrosPadrao.INCREMENTO_DE_FUNCIONARIOS);
        model.addAttribute("taxaprecedencia", GeradorParametrosPadrao.TAXA_DE_PRECEDENCIA);
        model.addAttribute("featuresiniciais", ParametrosPadrao.FEATURES_INICIAIS);
        model.addAttribute("numeroempregados", ParametrosPadrao.NUMERO_DE_EMPREGADOS);
        model.addAttribute("maximofeatures", ParametrosPadrao.MAXIMO_FEATURES);
        model.addAttribute("numerosemana", ParametrosIteracaoPadrao.NUMERO_DA_SEMANA);
        model.addAttribute("horassemana", ParametrosIteracaoPadrao.HORAS_DA_SEMANA);
        model.addAttribute("incrementofeature", ParametrosPadrao.INCREMENTO_DE_FEATURE);
        model.addAttribute("tamanhomaximoproblema", ParametrosPadrao.TAMANHO_MAXIMO_DO_PROBLEMA);
        model.addAttribute("tamanhoinicial", ParametrosPadrao.TAMANHO_INICIAL);
        model.addAttribute("incrementotamanho", ParametrosPadrao.INCREMENTO_DE_TAMANHO);
        model.addAttribute("qtdexecucoesinput", 2);

        return "/algoritmogeneticonrp/executar-experimento-nrp";
    }

    @PostMapping("/")
    public String executar(
                @RequestParam("maxfuncionarios") int maxfuncionarios,
                @RequestParam("funcionariosiniciais") int funcionariosiniciais,
                @RequestParam("numerofeatures") int numerofeatures,
                @RequestParam("avaliarhabilidadesfeatures") double avaliarhabilidadesfeatures,
                @RequestParam("reproducaoteste") int reproducaoteste,
                @RequestParam("incrementofuncionarios") int incrementofuncionarios,
                @RequestParam("taxaprecedencia") double taxaprecedencia,
                @RequestParam("featuresiniciais") int featuresiniciais,
                @RequestParam("numeroempregados") int numeroempregados,
                @RequestParam("maximofeatures") int maximofeatures,
                @RequestParam("numerosemana") int numerosemana,
                @RequestParam("horassemana") double horassemana,
                @RequestParam("incrementofeature") int incrementofeature,
                @RequestParam("tamanhomaximoproblema") int tamanhomaximoproblema,
                @RequestParam("tamanhoinicial") int tamanhoinicial,
                @RequestParam("incrementotamanho") int incrementotamanho,
                @RequestParam("qtdexecucoesinput") int qtdexecucoes,
                Model model


    ) throws IOException {

        for(int i = 0; i < qtdexecucoes; i++){

            Date inicio = new Date();
            Retorno retorno = launch(maxfuncionarios, funcionariosiniciais, numerofeatures, avaliarhabilidadesfeatures, reproducaoteste, incrementofuncionarios, taxaprecedencia, featuresiniciais, numeroempregados, maximofeatures, numerosemana, horassemana, incrementofeature, tamanhomaximoproblema, tamanhoinicial, incrementotamanho);

            List<String> resultados = retorno.getResultadoList();
            List<AlgoritmoExecucao> algoritmoExecucaoList = retorno.getAlgoritmoExecucaoList();

            Date fim = new Date();

            graficoBase64 = resultados.get(0);
            graficoBase64List.add(graficoBase64);

            String execucaonrp = resultados.get(1);
            execucoes += execucaonrp;

            //Salvar dados
            ExecucaoMetaheuristicas execucaoMetaheuristicas = ExecucaoMetaheuristicas.builder()
             //       .algoritmo(AlgoritmoEnum.TODOS)
                    .dataInicio(inicio)
                    .dataFim(fim)
                    .imagemGraficoBase64(graficoBase64)
                    .retornoExecucao(execucaonrp)
                    .maximoFuncionarios(maxfuncionarios)
                    .qtdFuncionariosIniciais(funcionariosiniciais)
                    .numfeatures(numerofeatures)
                    .avaliarHabilidadesFeatures(avaliarhabilidadesfeatures)
                    .reproducaoTeste(reproducaoteste)
                    .incrementoFuncionarios(incrementofuncionarios)
                    .taxaprecedencia(taxaprecedencia)
                    .featuresIniciais(featuresiniciais)
                    .numempregados(numeroempregados)
                    .maximoFeatures(maximofeatures)
                    .numsemanas(numerosemana)
                    .hrsporsemana(horassemana)
                    .incrementoFeature(incrementofeature)
                    .tamanhoMaximoProblema(tamanhomaximoproblema)
                    .tamanhoInicial(tamanhoinicial)
                    .incrementoTamanho(incrementotamanho)
                .build();

            for(AlgoritmoExecucao algoritmoExecucao : algoritmoExecucaoList){
                algoritmoExecucao.setExecucaoMetaheuristicas(execucaoMetaheuristicas);
            }

            //Adiciona a duração de cada algoritmo
            for(AlgoritmoExecucao algoritmoExecucao : algoritmoExecucaoList){
                Duration duracaoTotal = Duration.ZERO;
                for(AlgoritmoTempo algoritmoTempo : retorno.getAlgoritmoTempoList()){
                    if(algoritmoTempo.getAlgoritmo().equals(algoritmoExecucao.getAlgoritmoEnum())){
                        // Convertendo para Instant
                        Instant inicioInstant = algoritmoTempo.getInicio().toInstant();
                        Instant fimInstant = algoritmoTempo.getFim().toInstant();

                        // Calculando a diferença usando Duration
                        Duration duracao = Duration.between(inicioInstant, fimInstant);
                        duracaoTotal = duracaoTotal.plus(duracao);
                    }
                }
                algoritmoExecucao.setTempo(duracaoTotal);
            }

            execucaoMetaheuristicas.setAlgoritmoExecucaoList(algoritmoExecucaoList);

            execucaoAlgoritmoService.salvar(execucaoMetaheuristicas);

        }

        model.addAttribute("experimentonrp", "Teste");
        model.addAttribute("conteudoHtml", "Experimento NRP realizado com sucesso");
        model.addAttribute("graficollist", graficoBase64List);
        model.addAttribute("execucaonrp", execucoes);
        model.addAttribute("maxfuncionarios",  maxfuncionarios);
        model.addAttribute("funcionariosiniciais", funcionariosiniciais);
        model.addAttribute("numerofeatures", numerofeatures);
        model.addAttribute("avaliarhabilidadesfeatures", avaliarhabilidadesfeatures);
        model.addAttribute("reproducaoteste", reproducaoteste);
        model.addAttribute("incrementofuncionarios", incrementofuncionarios);
        model.addAttribute("taxaprecedencia", taxaprecedencia);
        model.addAttribute("featuresiniciais", featuresiniciais);
        model.addAttribute("numeroempregados", numeroempregados);
        model.addAttribute("maximofeatures", maximofeatures);
        model.addAttribute("numerosemana", numerosemana);
        model.addAttribute("horassemana", horassemana);
        model.addAttribute("incrementofeature", incrementofeature);
        model.addAttribute("tamanhomaximoproblema", tamanhomaximoproblema);
        model.addAttribute("tamanhoinicial", tamanhoinicial);
        model.addAttribute("incrementotamanho", incrementotamanho);
        model.addAttribute("qtdexecucoesinput", qtdexecucoes);



        return "/algoritmogeneticonrp/executar-experimento-nrp";
    }

    public Retorno launch(int maxfuncionarios, int funcionariosiniciais, int numerofeatures, double avaliarhabilidadesfeatures, int reproducaoteste, int incrementofuncionarios, double taxaprecedencia, int featuresiniciais, int numeroempregados, int maximofeatures, int numerosemana, double horassemana, int incrementofeature, int tamanhomaximoproblema, int tamanhoinicial, int incrementotamanho) {
        Retorno retorno = new Retorno();
        ExperimentController experimentController = new ExperimentController(TipoExperimento.FEATURES, maxfuncionarios, funcionariosiniciais, numerofeatures, avaliarhabilidadesfeatures, reproducaoteste, incrementofuncionarios, taxaprecedencia, featuresiniciais, numeroempregados, maximofeatures, numerosemana, horassemana, incrementofeature, tamanhomaximoproblema, tamanhoinicial, incrementotamanho);
        retorno.setResultadoList(experimentController.getResultados());
        retorno.setAlgoritmoExecucaoList(experimentController.getAlgoritmoExecucaoList());
        retorno.setAlgoritmoTempoList(experimentController.getAlgoritmoTempoList());
        return retorno;
    }
}
