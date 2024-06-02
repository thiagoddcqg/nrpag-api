package com.nextreleaseproblem.controller;

import com.nextreleaseproblem.dto.NovaModelagemDTO;
import com.nextreleaseproblem.model.novamodelagem.NovaModelagemEmployee;
import com.nextreleaseproblem.model.novamodelagem.NovaModelagemFeature;
import com.nextreleaseproblem.model.novamodelagem.NovaModelagemNRPModel;
import com.nextreleaseproblem.repository.entity.ExecucaoMetaheuristicas;
import com.nextreleaseproblem.service.novamodelagem.NovaModelagemGeneticAlgorithm;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
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

        List<NovaModelagemEmployee> employees = processarArquivoCsvEmpregados(file);

        // Criar modelo NRP com restrições máximas de esforço e número de features
        NovaModelagemNRPModel model = new NovaModelagemNRPModel(features, employees, 3, 20.0);

        // Executar o algoritmo genético
        NovaModelagemGeneticAlgorithm ga = new NovaModelagemGeneticAlgorithm(model, 100, 0.7, 0.01, 50);
        List<NovaModelagemFeature> bestSolution = ga.run();

        List<NovaModelagemDTO> lista = new ArrayList<>();
        // Exibir o resultado da melhor solução encontrada
        System.out.println("Melhor solução encontrada:");
        for (NovaModelagemFeature feature : bestSolution) {
            lista.add(NovaModelagemDTO.builder()
                            .id(feature.getId())
                            .valorNegocio(feature.getBusinessValue())
                            .esforco(feature.getEffort())
                    .build());
            System.out.println("ID: " + feature.getId() + ", Valor de Negócio: " + feature.getBusinessValue() + ", Esforço: " + feature.getEffort());
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(lista);
    }

    private List<NovaModelagemFeature> processarArquivoCsvFeatures(MultipartFile file) {

        List<NovaModelagemFeature> features = new ArrayList<>();

        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVReader csvReader = new CSVReaderBuilder(reader)
                     .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                     .build()) {

            String[] line;
            boolean readingFeatures = false;

            while ((line = csvReader.readNext()) != null) {

                if (line.length == 0) {
                    continue;
                }

                if (line[0].trim().contains("#")) {
                    if (line[0].contains("Features")) {
                        readingFeatures = true;
                    } else {
                        readingFeatures = false;
                    }
                    continue;
                }

                if (readingFeatures) {
                    NovaModelagemFeature feature = new NovaModelagemFeature(
                            Integer.parseInt(line[0]),
                            Double.parseDouble(line[1]),
                            Double.parseDouble(line[2]),
                            line[3].isEmpty() ? new ArrayList<>() : Arrays.stream(line[3].split(","))
                                    .map(Integer::parseInt)
                                    .collect(Collectors.toList()),
                            line[4],
                            line[5],
                            line[6],
                            line[7],
                            Integer.parseInt(line[8]),
                            LocalDate.parse(line[9]),
                            Integer.parseInt(line[10]),
                            Integer.parseInt(line[11])
                    );
                    features.add(feature);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing CSV file", e);
        }

        return features;
    }

    private List<NovaModelagemEmployee> processarArquivoCsvEmpregados(MultipartFile file) {

        List<NovaModelagemEmployee> empregados = new ArrayList<>();

        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVReader csvReader = new CSVReaderBuilder(reader)
                     .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                     .build()) {

            String[] line;
            boolean readingEmployees = false;

            while ((line = csvReader.readNext()) != null) {
                if (line.length == 0) {
                    continue;
                }

                String trimmedLine = line[0].trim();

                if (trimmedLine.contains("#")) {
                    if (trimmedLine.contains("Funcionário")) {
                        readingEmployees = true;
                    } else {
                        readingEmployees = false;
                    }
                    continue;
                }

                if (readingEmployees) {
                    NovaModelagemEmployee employee = new NovaModelagemEmployee(
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
