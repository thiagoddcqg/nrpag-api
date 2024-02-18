package com.nextreleaseproblem.controller;

import com.nextreleaseproblem.model.GeradorParametrosPadrao;
import com.nextreleaseproblem.model.GeradorParametros;
import com.nextreleaseproblem.service.ExecucaoAlgoritmoService;
import com.nextreleaseproblem.service.ExecucaoService;
import com.nextreleaseproblem.service.NextReleaseProblemAGService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;

import java.io.IOException;

import static com.nextreleaseproblem.model.GeradorNRP.gerar;
import static com.nextreleaseproblem.model.GeradorNRP.gravarArquivo;

@Controller
@RequiredArgsConstructor
@RequestMapping("/algoritmogeneticonrp/executar-gerador-nrp")
public class ExecutarGeradorNRPController {

    private final ExecucaoService execucaoService;

    private final NextReleaseProblemAGService nextReleaseProblemAGService;

    private final TemplateEngine templateEngine;

    private final ExecucaoAlgoritmoService service;

    @GetMapping("/")
    public String index(Model model) {
        return "/algoritmogeneticonrp/executar-gerador-nrp";
    }

    @PostMapping("/")
    public String executar(Model model) throws IOException {
        String htmlString = launch();

        model.addAttribute("conteudoHtml", htmlString);

        return "/algoritmogeneticonrp/executar-gerador-nrp";
    }

    public String launch() {
        //public static void inicializar() {
        int nbFeatures = 5;
        int nbEmployees = 2;
        int nbSkills = 1;
        gravarArquivo(gerar(new GeradorParametros(nbFeatures, nbEmployees, nbSkills, GeradorParametrosPadrao.TAXA_DE_PRECEDENCIA)));
        return "Arquivos gerados com sucesso";
    }
}
