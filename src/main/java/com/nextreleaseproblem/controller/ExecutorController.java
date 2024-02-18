package com.nextreleaseproblem.controller;

import com.nextreleaseproblem.model.ExecutorAlgoritmo;
import com.nextreleaseproblem.model.*;
import com.nextreleaseproblem.model.parametros.AlgoritmoEnum;
import com.nextreleaseproblem.model.parametros.ParametrosInteracao;
import com.nextreleaseproblem.model.parametros.DadosProblema;
import com.nextreleaseproblem.model.GeradorNRP;
import com.nextreleaseproblem.view.HTMLEscrita;
import com.nextreleaseproblem.view.QuadroParametros;

public class ExecutorController {
	
	public ExecutorController() {
		QuadroParametros frame = new QuadroParametros(this);
		frame.setVisible(true);
	}

	public void launch(AlgoritmoEnum algoritmoEnum, GeradorParametros genParam, ParametrosInteracao iterationParam) {
		DadosProblema dadosProblema =  GeradorNRP.gerar(genParam);
		NextReleaseProblem nrp = new NextReleaseProblem(dadosProblema.getFeatures(), dadosProblema.getFuncionarios(), iterationParam);
		ParametrosExecutor algoParam = new ParametrosExecutor();
		
		ExecutorAlgoritmo executor = new ExecutorAlgoritmo(nrp, algoParam);
		ResultadoExecucao result = executor.executeAlgorithm(algoritmoEnum);
		
		HTMLEscrita browserDisplay = new HTMLEscrita(result.getSolucao());
		browserDisplay.run();
	}

}
