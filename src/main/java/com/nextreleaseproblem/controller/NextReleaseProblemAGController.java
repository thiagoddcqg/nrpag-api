package com.nextreleaseproblem.controller;

import com.nextreleaseproblem.model.Funcionalidade;
import com.nextreleaseproblem.model.GeradorPDF;
import com.nextreleaseproblem.model.MetaheuristicaEnum;
import com.nextreleaseproblem.repository.entity.Execucao;
import com.nextreleaseproblem.service.ExecucaoService;
import com.nextreleaseproblem.service.NextReleaseProblemAGService;
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
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/metaheuristica/next-release-problem-ag")
public class NextReleaseProblemAGController {

    @Autowired
    private ExecucaoService execucaoService;

    private final NextReleaseProblemAGService nextReleaseProblemAGService;
    private String arquivoTxt = "";
    private String grafico = "";
    private String burndown = "";

    private final TemplateEngine templateEngine;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("metaheuristica", MetaheuristicaEnum.ALGORITMO_GENETICO);
        return "/metaheuristica/next-release-problem-ag";
    }

    @PostMapping("/")
    public String executar(@RequestParam("file") MultipartFile file,
                           @RequestParam("taxamutacaoinput") String taxamutacaoinput,
                           @RequestParam("populacaoinput") String populacaoinput,
                           @RequestParam("geracaoinput") String geracaoinput,
                           @RequestParam("torneioinput") String torneioinput,
                           @RequestParam("chancecruzamentoinput") String chancecruzamentoinput,
                           @RequestParam("tipocruzamento") String tipocruzamento,
                           @RequestParam("tiposelecao") String tiposelecao,
                           @RequestParam("duracaosprint") int duracaosprint,
                           @RequestParam("qtdexecucoesinput") int qtdexecucoesinput,
                           Model model) throws IOException {

        if(Objects.isNull(torneioinput) || Strings.isBlank(torneioinput))
            torneioinput = "0";

        Date inicio = new Date();
        System.out.println("Início: " + inicio);

        if(Strings.isBlank(taxamutacaoinput)
                || Strings.isBlank(populacaoinput)
                || Strings.isBlank(geracaoinput)
                || Strings.isBlank(chancecruzamentoinput)){
            model.addAttribute("metaheuristica", MetaheuristicaEnum.ALGORITMO_GENETICO);
            model.addAttribute("mensagemerro", "Erro: Por favor, preencha todos os campos.");
            return "/metaheuristica/next-release-problem-ag";
        }

        if (!file.getContentType().equals("text/csv")) {
            model.addAttribute("metaheuristica", MetaheuristicaEnum.ALGORITMO_GENETICO);
            model.addAttribute("mensagemerro", "Erro: Por favor, envie um arquivo CSV válido.");
            return "/metaheuristica/next-release-problem-ag";
        }

        List<Funcionalidade> funcionalidadeList = FileUtil.criarFeatures(file);

        model.addAttribute("mensagem", "Arquivo CSV enviado com sucesso!");

        String idExperimentacao = RuntimeUtil.generateIdExperimentacao(MetaheuristicaEnum.BUSCA_TABU);

        for(int i=0; i< qtdexecucoesinput; i++){
            List<String> retorno = nextReleaseProblemAGService.iniciar(funcionalidadeList,
                    taxamutacaoinput, populacaoinput, geracaoinput, torneioinput,
                    chancecruzamentoinput, tiposelecao, tipocruzamento, duracaosprint);

            Date fim = new Date();
            String tempo = RuntimeUtil.calcularTempoExecucao(inicio, fim);

            arquivoTxt = retorno.get(12);
            grafico = retorno.get(15);
            burndown = retorno.get(16);

            model.addAttribute("metaheuristica", MetaheuristicaEnum.ALGORITMO_GENETICO);
            model.addAttribute("tamanhopopulacao", retorno.get(0));
            model.addAttribute("maximageracao", retorno.get(1));
            model.addAttribute("taxamutacao", retorno.get(2));
            model.addAttribute("features", retorno.get(3));
            model.addAttribute("beneficios", retorno.get(4));
            model.addAttribute("provisao", retorno.get(5));
            model.addAttribute("geracaomelhoraptidao", retorno.get(6));
            model.addAttribute("melhorindividuoaptidao", retorno.get(7));
            model.addAttribute("recursobeneficio", retorno.get(8));
            model.addAttribute("somatoriopontosfuncao", retorno.get(9));
            model.addAttribute("qtdeFeaturesPorPrioridade", retorno.get(10));
            model.addAttribute("desenvolvedores", retorno.get(11));
            model.addAttribute("tamanhotorneio", retorno.get(13));
            model.addAttribute("qtdeacessofuncaoobjetivo", retorno.get(14));
            model.addAttribute("tempo", tempo);
            model.addAttribute("featureusadasprint", arquivoTxt);
            model.addAttribute("grafico", grafico);
            model.addAttribute("burndown", burndown);
            model.addAttribute("chanceCruzamento", retorno.get(17));
            model.addAttribute("qtdeSprints", retorno.get(18));
            model.addAttribute("pontosaptidaoproductbacklog", retorno.get(19));
            model.addAttribute("tiposelecao", retorno.get(20));
            model.addAttribute("tamanosolucao", retorno.get(21));


            Execucao execucaoAG = Execucao.builder()
                    .metaheuristica(MetaheuristicaEnum.ALGORITMO_GENETICO)
                    .dataInicio(inicio)
                    .dataFim(fim)
                    .taxaMutacao(taxamutacaoinput)
                    .tamPopulacao(populacaoinput)
                    .iteracoes(Integer.parseInt(geracaoinput))
                    .taxaCruzamento(chancecruzamentoinput)
                    .tipoCruzamento(tipocruzamento)
                    .tipoSelecao(tiposelecao)
                    .tamTorneio(torneioinput)
                    .valorSolucao(Integer.parseInt(retorno.get(7)))
                    .provisao(retorno.get(5))
                    .tamanhoSolucao(retorno.get(21))
                    .idExperimentacao(idExperimentacao)
                    .duracaoSprint(duracaosprint)
                    .provisao(retorno.get(5))
                    .build();

            execucaoService.salvarExecucao(execucaoAG);
        }

        return "/metaheuristica/next-release-problem-ag";
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
