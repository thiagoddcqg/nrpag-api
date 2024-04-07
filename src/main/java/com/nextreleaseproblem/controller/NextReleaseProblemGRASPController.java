package com.nextreleaseproblem.controller;

import com.nextreleaseproblem.model.Funcionalidade;
import com.nextreleaseproblem.model.GeradorPDF;
import com.nextreleaseproblem.model.MetaheuristicaEnum;
import com.nextreleaseproblem.repository.entity.Execucao;
import com.nextreleaseproblem.service.ExecucaoService;
import com.nextreleaseproblem.service.NextReleaseProblemGRASPService;
import com.nextreleaseproblem.util.FileUtil;
import com.nextreleaseproblem.util.RuntimeUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/metaheuristica/next-release-problem-grasp")
public class NextReleaseProblemGRASPController {

    final private NextReleaseProblemGRASPService NextReleaseProblemGRASPService;

    @Autowired
    private ExecucaoService execucaoService;

    private final TemplateEngine templateEngine;

    private String arquivoTxt = "";
    private String grafico = "";
    private String burndown = "";

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("metaheuristica", MetaheuristicaEnum.GRASP);
        return "/metaheuristica/next-release-problem-grasp";
    }

    @PostMapping("/")
    public String executar(@RequestParam("file") MultipartFile file,
                           @RequestParam("iteracoesinput") int iteracoesinput,
                           @RequestParam("alfainput") double alfainput,
                           @RequestParam("duracaosprint") int duracaosprint,
                           @RequestParam("tampopulacaoinput") int tampopulacaoinput,
                           @RequestParam("qtdexecucoesinput") int qtdexecucoesinput,
                           Model model) throws IOException {

        if(iteracoesinput == 0){
            model.addAttribute("metaheuristica", MetaheuristicaEnum.GRASP);
            model.addAttribute("mensagemerro", "Erro: Por favor, preencha todos os campos.");
            return "/metaheuristica/next-release-problem-grasp";
        }

        if (!file.getContentType().equals("text/csv")) {
            model.addAttribute("metaheuristica", MetaheuristicaEnum.GRASP);
            model.addAttribute("mensagemerro", "Erro: Por favor, envie um arquivo CSV válido.");
            return "/metaheuristica/next-release-problem-grasp";
        }

        model.addAttribute("mensagem", "Arquivo CSV enviado com sucesso!");

        String idExperimentacao = RuntimeUtil.generateIdExperimentacao(MetaheuristicaEnum.GRASP);

        List<Funcionalidade> funcionalidadeList = FileUtil.criarFeatures(file);

        for(int i=0; i< qtdexecucoesinput; i++){
            Date inicio = new Date();
            System.out.println("Início: " + inicio);
            List<String> retorno = NextReleaseProblemGRASPService.iniciar(funcionalidadeList, iteracoesinput, alfainput, duracaosprint, tampopulacaoinput);
            Date fim = new Date();
            String tempo = RuntimeUtil.calcularTempoExecucao(inicio, fim);

            arquivoTxt = retorno.get(3);
            grafico = retorno.get(7);
            burndown = retorno.get(8);

            model.addAttribute("metaheuristica", MetaheuristicaEnum.GRASP);
            model.addAttribute("geracaomelhoraptidao", retorno.get(1));
            model.addAttribute("desenvolvedores", retorno.get(2));
            model.addAttribute("featureusadasprint", arquivoTxt);
            model.addAttribute("recursobeneficio", retorno.get(4));
            model.addAttribute("interacoes", iteracoesinput);
            model.addAttribute("tempo", tempo);
            model.addAttribute("qtdeFeaturesPorPrioridade", retorno.get(5));
            model.addAttribute("provisao", retorno.get(6));
            model.addAttribute("grafico", grafico);
            model.addAttribute("burndown", burndown);


            Execucao execucaoGRASP = Execucao.builder()
                    .metaheuristica(MetaheuristicaEnum.GRASP)
                    .dataInicio(inicio)
                    .dataFim(fim)
                    .iteracoes(iteracoesinput)
                    .valorSolucao(Integer.valueOf(retorno.get(0)))
                    .alfa(alfainput)
                    .provisao(retorno.get(6))
                    .idExperimentacao(idExperimentacao)
                    .duracaoSprint(duracaosprint)
                    .tamPopulacao(String.valueOf(tampopulacaoinput))
                    .tamanhoSolucao(retorno.get(9))
                    .qtdeFeaturesPorPrioridade(retorno.get(10))
                    .tamanhoSolucao(retorno.get(11))
                    .qtdAcessoFuncaoObjetivo(Integer.valueOf(retorno.get(12)))
                    .build();

            execucaoService.salvarExecucao(execucaoGRASP);
        }

        return "metaheuristica/next-release-problem-grasp";
    }

    @GetMapping("/download")
    @ResponseBody
    public byte[] downloadFile() {

        return arquivoTxt.getBytes(StandardCharsets.UTF_8);
    }

    @GetMapping("/gerarpdf")
    public void generatePdf(Model model, HttpServletResponse response) throws Exception {
        Context context = new Context();
        context.setVariable("grafico", grafico);
        //    context.setVariable("generatingPdf", true); // Adicione este atributo

        byte[] pdfBytes = new GeradorPDF(templateEngine).generatePdfFromTemplate("modal/img-gantt", context);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=generated.pdf");
        response.setContentLength(pdfBytes.length);

        response.getOutputStream().write(pdfBytes);
    }

    @GetMapping("/gerarpdfbd")
    public void generatePdfBD(Model model, HttpServletResponse response) throws Exception {
        Context context = new Context();
        context.setVariable("burndown", burndown);
        //  context.setVariable("generatingPdf", true); // Adicione este atributo

        byte[] pdfBytes = new GeradorPDF(templateEngine).generatePdfFromTemplate("modal/img-burndown", context);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=generated.pdf");
        response.setContentLength(pdfBytes.length);

        response.getOutputStream().write(pdfBytes);
    }
}