package com.nextreleaseproblem.controller;

import com.nextreleaseproblem.repository.entity.Execucao;
import com.nextreleaseproblem.model.Funcionalidade;
import com.nextreleaseproblem.model.GeradorPDF;
import com.nextreleaseproblem.model.MetaheuristicaEnum;
import com.nextreleaseproblem.service.ExecucaoService;
import com.nextreleaseproblem.service.NextReleaseProblemBTService;
import com.nextreleaseproblem.util.FileUtil;
import com.nextreleaseproblem.util.RuntimeUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/metaheuristica/next-release-problem-bt")
public class NextReleaseProblemBTController {

    final private NextReleaseProblemBTService NextReleaseProblemBTService;

    @Autowired
    private ExecucaoService execucaoService;

    private final TemplateEngine templateEngine;

    private String arquivoTxt = "";
    private String grafico = "";
    private String burndown = "";

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("metaheuristica", MetaheuristicaEnum.BUSCA_TABU);
        return "/metaheuristica/next-release-problem-bt";
    }

    @PostMapping("/")
    public String executar(@RequestParam("file") MultipartFile file,
                           @RequestParam("iteracoesinput") int iteracoesinput,
                           @RequestParam("tamanhotabuinput") int tamanhotabuinput,
                           @RequestParam("qtdexecucoesinput") int qtdexecucoesinput,
                           @RequestParam("duracaosprint") int duracaosprint,
                           Model model) throws IOException {

        if(iteracoesinput == 0 || tamanhotabuinput ==0){
            model.addAttribute("metaheuristica", MetaheuristicaEnum.BUSCA_TABU);
            model.addAttribute("mensagemerro", "Erro: Por favor, preencha todos os campos.");
            return "/metaheuristica/next-release-problem-bt";
        }

        if (!file.getContentType().equals("text/csv")) {
            model.addAttribute("metaheuristica", MetaheuristicaEnum.BUSCA_TABU);
            model.addAttribute("mensagemerro", "Erro: Por favor, envie um arquivo CSV válido.");
            return "/metaheuristica/next-release-problem-bt";
        }

        model.addAttribute("mensagem", "Arquivo CSV enviado com sucesso!");

        String idExperimentacao = RuntimeUtil.generateIdExperimentacao(MetaheuristicaEnum.BUSCA_TABU);

        for(int i=0; i< qtdexecucoesinput; i++){
            List<Funcionalidade> funcionalidadeList = FileUtil.criarFeatures(file);
            Date inicio = new Date();
            System.out.println("Início: " + inicio);

            List<String> retorno = NextReleaseProblemBTService.iniciar(funcionalidadeList, iteracoesinput,
                    tamanhotabuinput, duracaosprint);

            Date fim = new Date();
            String tempo = RuntimeUtil.calcularTempoExecucao(inicio, fim);

            arquivoTxt = retorno.get(6);
            grafico = retorno.get(11);
            burndown = retorno.get(12);

            model.addAttribute("metaheuristica", MetaheuristicaEnum.BUSCA_TABU);
            model.addAttribute("tempo", tempo);
            model.addAttribute("interacoes", retorno.get(0));
            model.addAttribute("tamanhotabu", retorno.get(1));
            model.addAttribute("execucao", retorno.get(2));
            model.addAttribute("custo", retorno.get(3));
            model.addAttribute("desenvolvedores", retorno.get(4));
            model.addAttribute("geracaomelhoraptidao", retorno.get(5));
            model.addAttribute("qtdeFeaturesPorPrioridade", retorno.get(7));
            model.addAttribute("provisao", retorno.get(8));
            model.addAttribute("tamanosolucao", retorno.get(9));
            model.addAttribute("grafico", grafico);
            model.addAttribute("burndown", burndown);
            model.addAttribute("recursobeneficio", retorno.get(13));
            model.addAttribute("featureusadasprint", arquivoTxt);


            Execucao execucaoBT = Execucao.builder()
                    .metaheuristica(MetaheuristicaEnum.BUSCA_TABU)
                    .dataInicio(inicio)
                    .dataFim(fim)
                    .tamTabu(tamanhotabuinput)
                    .iteracoes(iteracoesinput)
                    .valorSolucao(Integer.parseInt(retorno.get(3)))
                    .qtdeFeaturesPorPrioridade(retorno.get(7))
                    .provisao(retorno.get(8))
                    .tamanhoSolucao(retorno.get(9))
                    .idExperimentacao(idExperimentacao)
                    .qtdAcessoFuncaoObjetivo(Integer.valueOf(retorno.get(10)))
                    .duracaoSprint(duracaosprint)
                    .build();

            execucaoService.salvarExecucao(execucaoBT);
        }

        return "metaheuristica/next-release-problem-bt";
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
