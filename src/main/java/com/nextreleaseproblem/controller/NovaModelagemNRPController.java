package com.nextreleaseproblem.controller;

import com.nextreleaseproblem.dto.NovaModelagemDTO;
import com.nextreleaseproblem.model.novamodelagem.NovaModelagemEmployee;
import com.nextreleaseproblem.model.novamodelagem.NovaModelagemFeature;
import com.nextreleaseproblem.model.novamodelagem.NovaModelagemNRPModel;
import com.nextreleaseproblem.repository.entity.ExecucaoMetaheuristicas;
import com.nextreleaseproblem.service.novamodelagem.NovaModelagemGeneticAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/novamodelagem/execucaonrp")
public class NovaModelagemNRPController {
    @PostMapping("/")
    public ResponseEntity<List<NovaModelagemDTO>> executar() {
        // Criar valores mockados para Features
        NovaModelagemFeature feature1 = new NovaModelagemFeature(1, 10.0, 5.0, Arrays.asList(), "Atividade", "Nova", "Feature 1", "Employee 1", 1, LocalDate.now(), 0, 0);
        NovaModelagemFeature feature2 = new NovaModelagemFeature(2, 15.0, 8.0, Arrays.asList(1), "Análise", "Nova", "Feature 2", "Employee 2", 1, LocalDate.now(), 0, 0);
        NovaModelagemFeature feature3 = new NovaModelagemFeature(3, 8.0, 3.0, Arrays.asList(), "Correção", "Nova", "Feature 3", "Employee 3", 1, LocalDate.now(), 0, 0);
        NovaModelagemFeature feature4 = new NovaModelagemFeature(4, 20.0, 12.0, Arrays.asList(2, 3), "Implementação", "Nova", "Feature 4", "Employee 4", 1, LocalDate.now(), 0, 0);

        List<NovaModelagemFeature> features = Arrays.asList(feature1, feature2, feature3, feature4);

        // Criar valores mockados para Employees
        NovaModelagemEmployee employee1 = new NovaModelagemEmployee(1, 40.0, Arrays.asList("Skill 1"));
        NovaModelagemEmployee employee2 = new NovaModelagemEmployee(2, 35.0, Arrays.asList("Skill 2"));
        NovaModelagemEmployee employee3 = new NovaModelagemEmployee(3, 45.0, Arrays.asList("Skill 3"));

        List<NovaModelagemEmployee> employees = Arrays.asList(employee1, employee2, employee3);

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
}
