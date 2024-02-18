package com.nextreleaseproblem.controller;

import com.nextreleaseproblem.repository.entity.Execucao;
import com.nextreleaseproblem.model.MetaheuristicaEnum;
import com.nextreleaseproblem.service.ExecucaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/execucao")
public class ExecucaoController {

    @Autowired
    private ExecucaoService execucaoService;

    @GetMapping("")
    public String index(Model model) {
        List<Execucao> execucaoList = execucaoService.listAllExecucao();
        model.addAttribute("metaheuristicas", MetaheuristicaEnum.values());
        model.addAttribute("execucoes", execucaoList);
        model.addAttribute("metaheuristicaEnum", MetaheuristicaEnum.EMPTY);
        return "/execucao";
    }

   @PostMapping("/buscar")
    public String consultarMetaheuristica(Model model, @RequestParam("metaheuristicaEnum") MetaheuristicaEnum metaheuristicaEnum) {
        List<Execucao> execucaoList = execucaoService.findByMetaheuristica(metaheuristicaEnum);
        model.addAttribute("metaheuristicas", MetaheuristicaEnum.values());
        model.addAttribute("execucoes", execucaoList);
        model.addAttribute("metaheuristicaEnum", metaheuristicaEnum);

        return "/execucao";
    }
}
