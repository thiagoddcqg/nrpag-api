package com.nextreleaseproblem.controller;

import com.nextreleaseproblem.model.ResultadoNRP;
import com.nextreleaseproblem.service.ExecucaoNRPService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/algoritmogeneticonrp/execucaonrp")
public class ExecucaoNRPController {

    private final ExecucaoNRPService execucaoNRPService;

    @GetMapping("")
    public String index(Model model) {
        List<ResultadoNRP> resultadoNRPList = execucaoNRPService.obterResultadoNRP();
        double totalTempoMedio = execucaoNRPService.totalTempo(resultadoNRPList);
        resultadoNRPList = execucaoNRPService.calcularProporcao(resultadoNRPList, totalTempoMedio);
        double totalProporcao = execucaoNRPService.totalProporcao(resultadoNRPList);
        model.addAttribute("resultadoNRPList", resultadoNRPList);
        model.addAttribute("totalTempoMedio", totalTempoMedio);
        model.addAttribute("totalProporcao", totalProporcao);
        return "/algoritmogeneticonrp/execucaonrp";
    }

}
