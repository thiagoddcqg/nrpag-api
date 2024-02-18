package com.nextreleaseproblem.controller;

import com.nextreleaseproblem.repository.entity.Execucao;
import com.nextreleaseproblem.model.Funcionalidade;
import com.nextreleaseproblem.model.GeradorPDF;
import com.nextreleaseproblem.model.MetaheuristicaEnum;
import com.nextreleaseproblem.service.ExecucaoService;
import com.nextreleaseproblem.service.NextReleaseProblemSAService;
import com.nextreleaseproblem.util.FileUtil;
import com.nextreleaseproblem.util.RuntimeUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/metaheuristica/next-release-problem-sa")
public class NextReleaseProblemSAController {

    final private NextReleaseProblemSAService NextReleaseProblemSAService;

    @Autowired
    private ExecucaoService execucaoService;

    private final TemplateEngine templateEngine;
    private String arquivoTxt = "";

    private String grafico = "";
    private String burndown = "";

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("metaheuristica", MetaheuristicaEnum.SIMULATED_ANNEALING);
        return "/metaheuristica/next-release-problem-sa";
    }

    @PostMapping("/")
    public String executar(@RequestParam("file") MultipartFile file,
                           @RequestParam("temperaturainicialinput") String temperaturainicialinput,
                           @RequestParam("taxaresfriamentoinput") String taxaresfriamentoinput,
                           @RequestParam("numeroiteracoesinput") String numeroiteracoesinput,
                           @RequestParam("duracaosprint") int duracaosprint,
                           @RequestParam("qtdexecucoesinput") int qtdexecucoesinput,
                           Model model) throws IOException {

        Date inicio = new Date();
        System.out.println("Início: " + inicio);

        if(Strings.isBlank(temperaturainicialinput)
                || Strings.isBlank(taxaresfriamentoinput)
                || Strings.isBlank(numeroiteracoesinput)){
            model.addAttribute("metaheuristica", MetaheuristicaEnum.SIMULATED_ANNEALING);
            model.addAttribute("mensagemerro", "Erro: Por favor, preencha todos os campos.");
            return "/metaheuristica/next-release-problem-sa";
        }

        if (!file.getContentType().equals("text/csv")) {
            model.addAttribute("metaheuristica", MetaheuristicaEnum.SIMULATED_ANNEALING);
            model.addAttribute("mensagemerro", "Erro: Por favor, envie um arquivo CSV válido.");
            return "/metaheuristica/next-release-problem-sa";
        }

        List<Funcionalidade> funcionalidadeList = FileUtil.criarFeatures(file);

        model.addAttribute("mensagem", "Arquivo CSV enviado com sucesso!");

        String idExperimentacao = RuntimeUtil.generateIdExperimentacao(MetaheuristicaEnum.SIMULATED_ANNEALING);

        for(int i=0; i< qtdexecucoesinput; i++){
            List<String> retorno = NextReleaseProblemSAService.iniciar(funcionalidadeList, temperaturainicialinput,
                    taxaresfriamentoinput, numeroiteracoesinput, duracaosprint);

            Date fim = new Date();
            String tempo = RuntimeUtil.calcularTempoExecucao(inicio, fim);

            arquivoTxt = retorno.get(11);
            grafico = retorno.get(14);
            burndown = retorno.get(15);

            model.addAttribute("metaheuristica", MetaheuristicaEnum.SIMULATED_ANNEALING);
            model.addAttribute("temperaturainicial", retorno.get(0));
            model.addAttribute("taxaresfriamento", retorno.get(1));
            model.addAttribute("numeroiteracoes", retorno.get(2));
            model.addAttribute("limiterecursos", retorno.get(3));
            model.addAttribute("solucaootima", retorno.get(4));
            model.addAttribute("tempo", tempo);
            model.addAttribute("execucao", retorno.get(5));
            model.addAttribute("tamanosolucao", retorno.get(6));
            model.addAttribute("provisao", retorno.get(7));
            model.addAttribute("valorsolucaootima", retorno.get(8));
            model.addAttribute("desenvolvedores", retorno.get(9));
            model.addAttribute("geracaomelhoraptidao", retorno.get(10));
            model.addAttribute("qtdeFeaturesPorPrioridade", retorno.get(12));
            model.addAttribute("grafico", grafico);
            model.addAttribute("burndown", burndown);
            model.addAttribute("recursobeneficio", retorno.get(16));
            model.addAttribute("featureusadasprint", arquivoTxt);

            Execucao execucaoSA = Execucao.builder()
                    .metaheuristica(MetaheuristicaEnum.SIMULATED_ANNEALING)
                    .dataInicio(inicio)
                    .dataFim(fim)
                    .temperaturaInicial(temperaturainicialinput)
                    .taxaResfriamento(taxaresfriamentoinput)
                    .iteracoes(Integer.parseInt(numeroiteracoesinput))
                    .valorSolucao(Integer.parseInt(retorno.get(8)))
                    .qtdeFeaturesPorPrioridade(retorno.get(12))
                    .tamanhoSolucao(retorno.get(6))
                    .provisao(retorno.get(7))
                    .idExperimentacao(idExperimentacao)
                    .qtdAcessoFuncaoObjetivo(Integer.valueOf(retorno.get(13)))
                    .duracaoSprint(duracaosprint)
                    .build();

            execucaoService.salvarExecucao(execucaoSA);
        }

        return "metaheuristica/next-release-problem-sa";
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
