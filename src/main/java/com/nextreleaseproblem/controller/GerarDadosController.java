package com.nextreleaseproblem.controller;

import com.nextreleaseproblem.model.GeradorNRP;
import com.nextreleaseproblem.model.GeradorParametros;
import com.nextreleaseproblem.model.parametros.DadosProblema;
import com.nextreleaseproblem.repository.DadosProblemaRepository;
import com.nextreleaseproblem.repository.entity.ExecucaoMetaheuristicas;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Data
@RequiredArgsConstructor
@RequestMapping("/algoritmogeneticonrp/gerar-dados")
public class GerarDadosController {

    private final DadosProblemaRepository dadosProblemaRepository;


    @GetMapping("/")
    public ResponseEntity<DadosProblema> lsitar() {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gerarAleatorio());
    }

    private DadosProblema gerarAleatorio(){
        GeradorParametros genParam = new GeradorParametros();
        genParam.setNumeroDeEmpregados(4);
        genParam.setNumeroDeFeatures(6);
        genParam.setNumeroDeHabilidades(4);
        genParam.setTaxaDeRestricoesDePrecedencia(0.2);
        DadosProblema dadosProblema =  GeradorNRP.gerar(genParam);
        dadosProblemaRepository.save(dadosProblema);
        return dadosProblema;
    }
}
