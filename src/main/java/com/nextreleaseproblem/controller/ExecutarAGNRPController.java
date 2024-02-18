package com.nextreleaseproblem.controller;

import com.nextreleaseproblem.model.ExecutorAlgoritmo;
import com.nextreleaseproblem.model.GeradorNRP;
import com.nextreleaseproblem.model.ResultadoExecucao;
import com.nextreleaseproblem.model.ParametrosExecutor;
import com.nextreleaseproblem.model.GeradorParametros;
import com.nextreleaseproblem.model.NextReleaseProblem;
import com.nextreleaseproblem.model.parametros.AlgoritmoEnum;
import com.nextreleaseproblem.model.parametros.ParametrosInteracao;
import com.nextreleaseproblem.model.parametros.DadosProblema;
import com.nextreleaseproblem.repository.entity.ExecucaoMetaheuristicas;
import com.nextreleaseproblem.service.ExecucaoService;
import com.nextreleaseproblem.service.ExecucaoAlgoritmoService;
import com.nextreleaseproblem.service.NextReleaseProblemAGService;
import com.nextreleaseproblem.view.HTMLEscrita;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;

import java.io.IOException;

@Controller
@Data
@RequiredArgsConstructor
@RequestMapping("/algoritmogeneticonrp/executar-ag-nrp")
public class ExecutarAGNRPController {

    private final ExecucaoService execucaoService;

    private final NextReleaseProblemAGService nextReleaseProblemAGService;

    private final TemplateEngine templateEngine;

    private final ExecucaoAlgoritmoService service;

    private int numeroSemanas = 3;
    private int horasSemanas = 35;
    private int numeroFeatures = 20;
    private int numeroEmpregados = 4;
    private int numeroHabilidades = 2;
    private double taxaPrecedencia = 0.3;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("algorithmChoices", AlgoritmoEnum.values());
        model.addAttribute("numerosemanas", numeroSemanas);
        model.addAttribute("horassemanas", horasSemanas);
        model.addAttribute("numerofeatures", numeroFeatures);
        model.addAttribute("numeroempregados", numeroEmpregados);
        model.addAttribute("numerohabilidades", numeroHabilidades);
        model.addAttribute("taxaprecedencia", taxaPrecedencia);

        return "/algoritmogeneticonrp/executar-ag-nrp";
    }

    @PostMapping("/")
    public String executar(@RequestParam("algoritmoinput") String algoritmoinput,
                           @RequestParam("numsemanasinput") int numsemanasinput,
                           @RequestParam("hrsporsemanainput") double hrsporsemanainput,
                           @RequestParam("numfeaturesinput") int numfeaturesinput,
                           @RequestParam("numempregadosinput") int numempregadosinput,
                           @RequestParam("numerohabilidadesinput") int numerohabilidadesinput,
                           @RequestParam("taxaprecedenciainput") double taxaprecedenciainput,
                           Model model) throws IOException {


        AlgoritmoEnum algoritmoEnum = AlgoritmoEnum.valueOf(algoritmoinput);
        GeradorParametros genParam = new GeradorParametros(numfeaturesinput, numempregadosinput, numerohabilidadesinput, taxaprecedenciainput);
        ParametrosInteracao iterationParam = new ParametrosInteracao(numsemanasinput, hrsporsemanainput);

        String htmlString = launch(algoritmoEnum, genParam, iterationParam);

        ExecucaoMetaheuristicas execucaoMetaheuristicas = ExecucaoMetaheuristicas.builder()
                .algoritmo(AlgoritmoEnum.valueOf(algoritmoinput))
                .hrsporsemana(hrsporsemanainput)
                .numempregados(numempregadosinput)
                .numfeatures(numfeaturesinput)
                .numsemanas(numsemanasinput)
                .taxaprecedencia(taxaprecedenciainput)
                .numerohabilidades(numerohabilidadesinput)
                .htmlstring(htmlString)
            .build();

        service.salvar(execucaoMetaheuristicas);

        model.addAttribute("algorithmChoices", AlgoritmoEnum.values());
        model.addAttribute("conteudoHtml", htmlString);
        model.addAttribute("numerosemanas", numsemanasinput);
        model.addAttribute("horassemanas", hrsporsemanainput);
        model.addAttribute("numerofeatures", numfeaturesinput);
        model.addAttribute("numeroempregados", numempregadosinput);
        model.addAttribute("numerohabilidades", numerohabilidadesinput);
        model.addAttribute("taxaprecedencia", taxaprecedenciainput);

        return "/algoritmogeneticonrp/executar-ag-nrp";
    }

    public String launch(AlgoritmoEnum algoritmoEnum, GeradorParametros genParam, ParametrosInteracao iterationParam) {
        DadosProblema dadosProblema =  GeradorNRP.gerar(genParam);
        NextReleaseProblem nrp = new NextReleaseProblem(dadosProblema.getFeatures(), dadosProblema.getFuncionarios(), iterationParam);
        ParametrosExecutor algoParam = new ParametrosExecutor();

        ExecutorAlgoritmo executor = new ExecutorAlgoritmo(nrp, algoParam);
        ResultadoExecucao result = executor.executeAlgorithm(algoritmoEnum);

        HTMLEscrita browserDisplay = new HTMLEscrita(result.getSolucao());
        return browserDisplay.getCodigoPaginaHTML();
    }
}
