package com.nextreleaseproblem.controller;

import com.nextreleaseproblem.model.Funcionalidade;
import com.nextreleaseproblem.model.Release;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dataset")
public class DatasetController {

    @GetMapping("")
    public String index(Model model) {
        return "/dataset";
    }

   @PostMapping("")
    public String gerar(Model model,
                        @RequestParam("qrdsprintinput") int qrdsprintinput,
                        @RequestParam("pornivelinput") String pornivelinput,
                        @RequestParam("desnametextarea") String desnametextarea) {

        List<Funcionalidade> funcionalidadeList = Release.gerarFeatures(qrdsprintinput, pornivelinput, desnametextarea);

        return "/dataset";
    }
}
