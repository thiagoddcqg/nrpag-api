package com.nextreleaseproblem.controller;

import com.nextreleaseproblem.repository.ExecucaoAlgoritmoRepository;
import com.nextreleaseproblem.repository.entity.ExecucaoMetaheuristicas;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teste")
public class TesteController {

    private final ExecucaoAlgoritmoRepository execucaoAlgoritmoRepository;

    @GetMapping("")
    public String teste() {
        return "Sucesso!, conexão estabalecida.";
    }

    @GetMapping("/dados")
    public List<Teste> listar(){
        List<Teste> testeList = new ArrayList<Teste>();
        testeList.add(Teste.builder().nome("João").idade(20).build());
        testeList.add(Teste.builder().nome("Maria").idade(22).build());

        return testeList;
    }

    @Data
    @Builder
    public static class Teste{
        private String nome;
        private Integer idade;
    }

}
