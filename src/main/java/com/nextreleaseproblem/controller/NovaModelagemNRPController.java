package com.nextreleaseproblem.controller;

import com.nextreleaseproblem.dto.NovaModelagemDTO;
import com.nextreleaseproblem.model.novamodelagem.NovaModelagemFuncionario;
import com.nextreleaseproblem.model.novamodelagem.NovaModelagemFeature;
import com.nextreleaseproblem.model.novamodelagem.NovaModelagemNRPModelo;
import com.nextreleaseproblem.service.novamodelagem.NovaModelagemAlgoritmoGenetico;
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
        NovaModelagemFeature feature1 = new NovaModelagemFeature(1, 10.0, 5.0, Arrays.asList(), "Atividade", "Nova", "Feature 1", "Funcionario 1", 1, LocalDate.now(), 0, 0);
        NovaModelagemFeature feature2 = new NovaModelagemFeature(2, 15.0, 8.0, Arrays.asList(1), "Análise", "Nova", "Feature 2", "Funcionario 2", 1, LocalDate.now(), 0, 0);
        NovaModelagemFeature feature3 = new NovaModelagemFeature(3, 8.0, 3.0, Arrays.asList(), "Correção", "Nova", "Feature 3", "Funcionario 3", 1, LocalDate.now(), 0, 0);
        NovaModelagemFeature feature4 = new NovaModelagemFeature(4, 20.0, 12.0, Arrays.asList(2, 3), "Implementação", "Nova", "Feature 4", "Funcionario 4", 1, LocalDate.now(), 0, 0);

        List<NovaModelagemFeature> features = Arrays.asList(feature1, feature2, feature3, feature4);

        // Criar valores mockados para Employees
        NovaModelagemFuncionario funcionario1 = new NovaModelagemFuncionario(1, 40.0, Arrays.asList("Skill 1"));
        NovaModelagemFuncionario funcionario2 = new NovaModelagemFuncionario(2, 35.0, Arrays.asList("Skill 2"));
        NovaModelagemFuncionario funcionario3 = new NovaModelagemFuncionario(3, 45.0, Arrays.asList("Skill 3"));

        List<NovaModelagemFuncionario> funcionarios = Arrays.asList(funcionario1, funcionario2, funcionario3);

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
}
