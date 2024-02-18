package com.nextreleaseproblem.controller;

import com.nextreleaseproblem.model.CarregadorDados;
import com.nextreleaseproblem.model.ArquivoTeste;
import com.nextreleaseproblem.model.*;
import com.nextreleaseproblem.model.comparadores.CompararadorSolucaoPlaneamentoViolacaoRestricao;
import com.nextreleaseproblem.model.operadores.OperadorCruzamentoPlanejamento;
import com.nextreleaseproblem.model.operadores.OperadorMutacaoPlanejamento;
import com.nextreleaseproblem.model.parametros.ParametrosInteracao;
import com.nextreleaseproblem.model.parametros.ParametrosPadrao;
import com.nextreleaseproblem.model.parametros.DadosProblema;
import com.nextreleaseproblem.service.ExecucaoAlgoritmoService;
import com.nextreleaseproblem.service.ExecucaoService;
import com.nextreleaseproblem.service.NextReleaseProblemAGService;
import com.nextreleaseproblem.view.HTMLEscrita;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.util.AlgorithmRunner;

import java.io.IOException;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/algoritmogeneticonrp/executar-programa-nrp")
public class ExecutarProgramaNRPController {

    private final ExecucaoService execucaoService;

    private final NextReleaseProblemAGService nextReleaseProblemAGService;

    private final TemplateEngine templateEngine;

    private final ExecucaoAlgoritmoService service;

    @GetMapping("/")
    public String index(Model model) {
        return "/algoritmogeneticonrp/executar-programa-nrp";
    }

    @PostMapping("/")
    public String executar(Model model) throws IOException {
        String htmlString = launch();

        model.addAttribute("conteudoHtml", htmlString);

        return "/algoritmogeneticonrp/executar-programa-nrp";
    }

    public String launch() {
        DadosProblema data = CarregadorDados.lerDados(ArquivoTeste.OTIMIZACAO_EXCESSO);

        NextReleaseProblem problem = new NextReleaseProblem(data.getFeatures(), data.getFuncionarios(), new ParametrosInteracao(1, 35.0));
        Algorithm<List<PlanejamentoSolucao>> algorithm;
        CrossoverOperator<PlanejamentoSolucao> crossover;
        MutationOperator<PlanejamentoSolucao> mutation;
        SelectionOperator<List<PlanejamentoSolucao>, PlanejamentoSolucao> selection;

        crossover = new OperadorCruzamentoPlanejamento(problem);

        mutation = new OperadorMutacaoPlanejamento(problem);

        selection = new BinaryTournamentSelection<>(new CompararadorSolucaoPlaneamentoViolacaoRestricao());

        algorithm = new NSGAIIBuilder<PlanejamentoSolucao>(problem, crossover, mutation)
                .setSelectionOperator(selection)
                .setMaxIterations(ParametrosPadrao.NUMBER_OF_ITERATIONS)
                .setPopulationSize(ParametrosPadrao.POPULATION_SIZE)
                .build();

        new AlgorithmRunner.Executor(algorithm).execute();

        List<PlanejamentoSolucao> population = algorithm.getResult();
        Set<PlanejamentoSolucao> filteredPopulation = FiltroPopulacao.getMelhoresSolucoes(population);
        printPopulation(population);
        printPopulation(filteredPopulation);

        HTMLEscrita browserDisplay = new HTMLEscrita(new ArrayList<>(filteredPopulation));
        //browserDisplay.run();
        return browserDisplay.getCodigoPaginaHTML();
    }

    public static void printPopulation(Collection<PlanejamentoSolucao> population) {
        int solutionCpt = 1;
        Iterator<PlanejamentoSolucao> iterator = population.iterator();

        while (iterator.hasNext()) {
            PlanejamentoSolucao currentSolution = iterator.next();
            System.out.println("Solution " + solutionCpt++ + ": " + currentSolution);
        }
    }
}
