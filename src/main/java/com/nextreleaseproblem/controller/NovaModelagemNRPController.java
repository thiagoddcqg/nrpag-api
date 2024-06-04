package com.nextreleaseproblem.controller;

import com.nextreleaseproblem.dto.NovaModelagemDTO;
import com.nextreleaseproblem.enuns.PrioridadeFeature;
import com.nextreleaseproblem.enuns.StatusFeature;
import com.nextreleaseproblem.exception.RegraDeNegocioException;
import com.nextreleaseproblem.model.novamodelagem.NovaModelagemFuncionario;
import com.nextreleaseproblem.model.novamodelagem.NovaModelagemFeature;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.nextreleaseproblem.model.novamodelagem.NovaModelagemNRPModelo;
import com.nextreleaseproblem.service.novamodelagem.NovaModelagemAlgoritmoGenetico;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/novamodelagem/execucaonrp")
public class NovaModelagemNRPController {
    @PostMapping("/")
    public ResponseEntity<List<NovaModelagemDTO>> executar(@RequestParam("file") MultipartFile file) {

        List<NovaModelagemFeature> features = processarArquivoCsvFeatures(file);
        validarDataInicioManorDataFim(features);

        List<NovaModelagemFuncionario> funcionarios = processarArquivoCsvEmpregados(file);

        // Criar modelo NRP com restrições máximas de esforço e número de features
        NovaModelagemNRPModelo model = new NovaModelagemNRPModelo(features, funcionarios, 3, 20.0);

        // Executar o algoritmo genético
        NovaModelagemAlgoritmoGenetico ag = new NovaModelagemAlgoritmoGenetico(model, 100, 0.7, 0.01, 50);
        List<NovaModelagemFeature> melhorSolucao = ag.executar();

        List<NovaModelagemDTO> lista = new ArrayList<>();
        // Exibir o resultado da melhor solução encontrada
        System.out.println("Melhor solução encontrada:");
        for (NovaModelagemFeature feature : melhorSolucao) {
            lista.add(NovaModelagemDTO.builder()
                            .id(feature.getId())
                            .valorNegocio(feature.getValorNegocio())
                            .esforco(feature.getEsforco())
                    .build());
            System.out.println("ID: " + feature.getId() + ", Valor de Negócio: " + feature.getValorNegocio() + ", Esforço: " + feature.getEsforco());
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(lista);
    }

    private void validarDataInicioManorDataFim(List<NovaModelagemFeature> features){
        for(NovaModelagemFeature feature :  features){
            if(feature.getDataInicio().isAfter(feature.getDataFim())){
                throw new RegraDeNegocioException("A data inicial não pode ser depois que a data fim!");
            }
        }
    }


    private List<NovaModelagemFeature> processarArquivoCsvFeatures(MultipartFile file) {

        List<NovaModelagemFeature> features = new ArrayList<>();

        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVReader csvReader = new CSVReaderBuilder(reader)
                     .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                     .build()) {

            String[] line;
            boolean lendoFeatures = false;

            while ((line = csvReader.readNext()) != null) {

                if (line.length == 0) {
                    continue;
                }

                if (line[0].trim().contains("#")) {
                    if (line[0].contains("Features")) {
                        lendoFeatures = true;
                    } else {
                        lendoFeatures = false;
                    }
                    continue;
                }

                if (lendoFeatures) {
                    NovaModelagemFeature feature = new NovaModelagemFeature(
                            Integer.parseInt(line[0]),
                            Double.parseDouble(line[1]),
                            Double.parseDouble(line[2]),
                            line[3].isEmpty() ? new ArrayList<>() : Arrays.stream(line[3].split(","))
                                    .map(Integer::parseInt)
                                    .collect(Collectors.toList()),
                            line[4],
                            StatusFeature.valueOf(line[5]),
                            line[6],
                            line[7],
                            Integer.parseInt(line[8]),
                            LocalDate.parse(line[9]),
                            LocalDate.parse(line[10]),
                            Integer.parseInt(line[11]),
                            Integer.parseInt(line[12]),
                            PrioridadeFeature.valueOf(line[13])
                    );
                    features.add(feature);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing CSV file", e);
        }

        return features;
    }

    private List<NovaModelagemFuncionario> processarArquivoCsvEmpregados(MultipartFile file) {

        List<NovaModelagemFuncionario> empregados = new ArrayList<>();

        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVReader csvReader = new CSVReaderBuilder(reader)
                     .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                     .build()) {

            String[] line;
            boolean lendoFuncionarios = false;

            while ((line = csvReader.readNext()) != null) {
                if (line.length == 0) {
                    continue;
                }

                String trimmedLine = line[0].trim();

                if (trimmedLine.contains("#")) {
                    if (trimmedLine.contains("Funcionário")) {
                        lendoFuncionarios = true;
                    } else {
                        lendoFuncionarios = false;
                    }
                    continue;
                }

                if (lendoFuncionarios) {
                    NovaModelagemFuncionario employee = new NovaModelagemFuncionario(
                            Integer.parseInt(line[0].trim()),
                            Double.parseDouble(line[1].trim()),
                            Arrays.asList(line[2].trim())
                    );
                    empregados.add(employee);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing CSV file", e);
        }

        return empregados;
    }

}
