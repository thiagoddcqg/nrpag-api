package com.nextreleaseproblem.controller;

import com.nextreleaseproblem.dto.RetornoDadosDTO;
import com.nextreleaseproblem.model.*;
import com.nextreleaseproblem.model.parametros.AlgoritmoEnum;
import com.nextreleaseproblem.model.parametros.DadosProblema;
import com.nextreleaseproblem.model.parametros.ParametrosInteracao;
import com.nextreleaseproblem.repository.entity.ExecucaoMetaheuristicas;
import com.nextreleaseproblem.service.ExecucaoAlgoritmoService;
import com.nextreleaseproblem.service.ExecucaoService;
import com.nextreleaseproblem.service.NextReleaseProblemAGService;
import com.nextreleaseproblem.util.HtmlUtils;
import com.nextreleaseproblem.view.HTMLEscrita;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.json.JSONObject;
import org.jsoup.select.Elements;
import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@RestController
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

   /* @GetMapping("/")
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
*/
    @GetMapping("/")
    public ResponseEntity<List<ExecucaoMetaheuristicas>> lsitar() {
       List<ExecucaoMetaheuristicas> list = service.listar();
       return ResponseEntity
               .status(HttpStatus.OK)
               .body(list);
    }

    /*  @PostMapping("/")
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

          // "/algoritmogeneticonrp/executar-ag-nrp"
          return htmlString;
      }
  */
/*    @PostMapping("/")
    public ResponseEntity<String> executar(@RequestParam("algoritmoinput") String algoritmoinput,
                                           @RequestParam("numsemanasinput") int numsemanasinput,
                                           @RequestParam("hrsporsemanainput") double hrsporsemanainput,
                                           @RequestParam("numfeaturesinput") int numfeaturesinput,
                                           @RequestParam("numempregadosinput") int numempregadosinput,
                                           @RequestParam("numerohabilidadesinput") int numerohabilidadesinput,
                                           @RequestParam("taxaprecedenciainput") double taxaprecedenciainput) {


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

     *//*   model.addAttribute("algorithmChoices", AlgoritmoEnum.values());
        model.addAttribute("conteudoHtml", htmlString);
        model.addAttribute("numerosemanas", numsemanasinput);
        model.addAttribute("horassemanas", hrsporsemanainput);
        model.addAttribute("numerofeatures", numfeaturesinput);
        model.addAttribute("numeroempregados", numempregadosinput);
        model.addAttribute("numerohabilidades", numerohabilidadesinput);
        model.addAttribute("taxaprecedencia", taxaprecedenciainput);*//*

        return ResponseEntity.ok(htmlString);
    }*/

/*    @PostMapping("/")
    public ResponseEntity<String> executar(@RequestParam("algoritmoinput") String algoritmoinput,
                                           @RequestParam("numsemanasinput") int numsemanasinput,
                                           @RequestParam("hrsporsemanainput") double hrsporsemanainput,
                                           @RequestParam("numfeaturesinput") int numfeaturesinput,
                                           @RequestParam("numempregadosinput") int numempregadosinput,
                                           @RequestParam("numerohabilidadesinput") int numerohabilidadesinput,
                                           @RequestParam("taxaprecedenciainput") double taxaprecedenciainput) {


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

        // "/algoritmogeneticonrp/executar-ag-nrp"
        //return htmlString;

        // Parse the HTML document
        Document doc = Jsoup.parse(htmlString);

        // Create a JSON object to store the converted data
        JSONObject jsonObject = new JSONObject();

        // Print the JSON object
        System.out.println(jsonObject.toString());

        return ResponseEntity.ok(HtmlUtils.convertElementToJson(doc.body(), jsonObject).toString());
    }*/

   /* @PostMapping("/")
    public ResponseEntity<RetornoDadosDTO> executar(@RequestParam("algoritmoinput") String algoritmoinput,
                                           @RequestParam("numsemanasinput") int numsemanasinput,
                                           @RequestParam("hrsporsemanainput") double hrsporsemanainput,
                                           @RequestParam("numfeaturesinput") int numfeaturesinput,
                                           @RequestParam("numempregadosinput") int numempregadosinput,
                                           @RequestParam("numerohabilidadesinput") int numerohabilidadesinput,
                                           @RequestParam("taxaprecedenciainput") double taxaprecedenciainput) {


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

        RetornoDadosDTO retornoDadosDTO = RetornoDadosDTO.builder()
                .algoritmo(algoritmoinput)
                .htmlString(htmlString)
                .quantidadeSemanas(numsemanasinput)
                .quantidadeHorasSemana(hrsporsemanainput)
                .quantidadeFeatures(numfeaturesinput)
                .quantidadeEmpregados(numempregadosinput)
                .quantidadeHabitantes(numerohabilidadesinput)
                .taxaPrecedencia(taxaprecedenciainput)
            .build();

     *//*   model.addAttribute("algorithmChoices", AlgoritmoEnum.values());
        model.addAttribute("conteudoHtml", htmlString);
        model.addAttribute("numerosemanas", numsemanasinput);
        model.addAttribute("horassemanas", hrsporsemanainput);
        model.addAttribute("numerofeatures", numfeaturesinput);
        model.addAttribute("numeroempregados", numempregadosinput);
        model.addAttribute("numerohabilidades", numerohabilidadesinput);
        model.addAttribute("taxaprecedencia", taxaprecedenciainput);
     *//*

        return ResponseEntity.ok(retornoDadosDTO);
    }*/

    @PostMapping(value = "/", produces = "application/json")
    public ResponseEntity<String> executar(@RequestParam("algoritmoinput") String algoritmoinput,
                                                    @RequestParam("numsemanasinput") int numsemanasinput,
                                                    @RequestParam("hrsporsemanainput") double hrsporsemanainput,
                                                    @RequestParam("numfeaturesinput") int numfeaturesinput,
                                                    @RequestParam("numempregadosinput") int numempregadosinput,
                                                    @RequestParam("numerohabilidadesinput") int numerohabilidadesinput,
                                                    @RequestParam("taxaprecedenciainput") double taxaprecedenciainput) {

        Date inicio = new Date();

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

        Document doc = Jsoup.parse(htmlString);
        Element table = doc.select("table").first();
        String html = table != null ? table.outerHtml() : "";

        String json = convertHtmlTableToJson(html);

        execucaoMetaheuristicas.setRetornoExecucao(json);
        execucaoMetaheuristicas.setDataInicio(inicio);
        execucaoMetaheuristicas.setDataFim(new Date());

        service.salvar(execucaoMetaheuristicas);


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(json);
    }

    private static String convertHtmlTableToJson(String html) {
        Document doc = Jsoup.parse(html);
        Elements rows = doc.select("table tr");

        Gson gson = new Gson();
        StringBuilder json = new StringBuilder("[");
        for (int i = 1; i < rows.size(); i++) { // Start from 1 to skip header row
            Element row = rows.get(i);
            Elements cols = row.select("td");
            if (cols.size() == 0) continue; // Skip empty rows

            StringBuilder rowData = new StringBuilder("{");
            for (int j = 0; j < cols.size(); j++) {
                String header = rows.get(0).select("th").get(j).text();
                String value = cols.get(j).text();
                rowData.append("\"").append(header).append("\":\"").append(value).append("\",");
            }
            rowData.deleteCharAt(rowData.length() - 1); // Remove trailing comma
            rowData.append("}");

            json.append(rowData).append(",");
        }
        json.deleteCharAt(json.length() - 1); // Remove trailing comma
        json.append("]");

        return json.toString();
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