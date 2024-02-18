package com.nextreleaseproblem.controller;

import com.nextreleaseproblem.model.Funcionalidade;
import com.nextreleaseproblem.model.MetaheuristicaEnum;
import com.nextreleaseproblem.repository.entity.Execucao;
import com.nextreleaseproblem.util.FileUtil;
import com.nextreleaseproblem.util.RuntimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/algoritmogeneticonrp/cadastrar-feature")
public class CadastrarFeatureController {

    @GetMapping("/")
    public String index(Model model) {
        return "/algoritmogeneticonrp/cadastrar-feature";
    }

    @PostMapping("/")
    public String cadastrar(@RequestParam("iteracoesinput") int iteracoesinput,
                            Model model) throws IOException {

//        if (iteracoesinput == 0 || tamanhotabuinput == 0) {
//            model.addAttribute("metaheuristica", MetaheuristicaEnum.BUSCA_TABU);
//            model.addAttribute("mensagemerro", "Erro: Por favor, preencha todos os campos.");
//            return "/metaheuristica/next-release-problem-bt";
//        }

        model.addAttribute("mensagem", "Feature cadastrada com sucesso!");

//        List<String> retorno = NextReleaseProblemBTService.iniciar(funcionalidadeList, iteracoesinput,
//                tamanhotabuinput, duracaosprint);
//
//        model.addAttribute("metaheuristica", MetaheuristicaEnum.BUSCA_TABU);
//        model.addAttribute("tempo", tempo);
//        model.addAttribute("interacoes", retorno.get(0));
//
//        Execucao execucaoBT = Execucao.builder()
//                .metaheuristica(MetaheuristicaEnum.BUSCA_TABU)
//                .dataInicio(inicio)
//                .dataFim(fim)
//                .tamTabu(tamanhotabuinput)
//                .iteracoes(iteracoesinput)
//                .valorSolucao(Integer.parseInt(retorno.get(3)))
//                .qtdeFeaturesPorPrioridade(retorno.get(7))
//                .provisao(retorno.get(8))
//                .tamanhoSolucao(retorno.get(9))
//                .idExperimentacao(idExperimentacao)
//                .qtdAcessoFuncaoObjetivo(Integer.valueOf(retorno.get(10)))
//                .duracaoSprint(duracaosprint)
//                .build();
//
//        execucaoService.salvarExecucao(execucaoBT);

        return "/algoritmogeneticonrp/cadastrar-feature/";
    }
}
