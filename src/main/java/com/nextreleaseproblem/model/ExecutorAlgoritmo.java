package com.nextreleaseproblem.model;

import com.nextreleaseproblem.model.operadores.OperadorCruzamentoPlanejamento;
import com.nextreleaseproblem.model.operadores.OperadorMutacaoPlanejamento;
import com.nextreleaseproblem.model.parametros.AlgoritmoEnum;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.mocell.MOCellBuilder;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.algorithm.multiobjective.pesa2.PESA2Builder;
import org.uma.jmetal.algorithm.multiobjective.smsemoa.SMSEMOABuilder;
import org.uma.jmetal.algorithm.multiobjective.spea2.SPEA2Builder;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.util.AlgorithmRunner;

import java.util.ArrayList;
import java.util.List;

public class ExecutorAlgoritmo {
	
	/**
	 * Solução do problema
	 */
	private NextReleaseProblem problema;
	
	/**
	 * Parâmetros dos algoritmos
	 */
	private ParametrosExecutor parametros;

	/**
	 * @param problema
	 */
	public ExecutorAlgoritmo(NextReleaseProblem problema, ParametrosExecutor parametros) {
		this.problema = problema;
		this.parametros = parametros;
	}
	
	/**
	 * Executa um problema passado para o construtor
	 * @param algoritmoEnum O algoritmo a ser executado
	 * @return o resultado da execução do algoritmo
	 */
	public ResultadoExecucao executeAlgorithm(AlgoritmoEnum algoritmoEnum) {
		ResultadoExecucao resultado = null;
		List<PlanejamentoSolucao> populacao = null;
		CrossoverOperator<PlanejamentoSolucao> solucaoPlanejamento = new OperadorCruzamentoPlanejamento(problema);
	    MutationOperator<PlanejamentoSolucao> operacaoMutacao = new OperadorMutacaoPlanejamento(problema);
	    
		switch (algoritmoEnum) {
			/*case GENERATIONAL: {
				Algorithm<PlanejamentoSolucao> algorithm = new GeneticAlgorithmBuilder<PlanejamentoSolucao>(problema, solucaoPlanejamento, operacaoMutacao)
					.setMaxEvaluations(parametros.getTamPopulacao() * parametros.getQtdeInteracao())
					.setPopulationSize(parametros.getTamPopulacao())
					.build();

				populacao = new ArrayList<>(1);
				new AlgorithmRunner.Executor(algorithm).execute();
				populacao.add(algorithm.getResult());
				break;
			}*/
			case MOCell: {
				Algorithm<List<PlanejamentoSolucao>> algoritmo = new MOCellBuilder<PlanejamentoSolucao>(problema, solucaoPlanejamento, operacaoMutacao)
					.setMaxEvaluations(parametros.getQtdeInteracao())
					.setPopulationSize(parametros.getTamPopulacao())
					.build();
				AlgorithmRunner executor = new AlgorithmRunner.Executor(algoritmo).execute();
				resultado = new ResultadoExecucao(FiltroPopulacao.getMelhorSolucao(algoritmo.getResult()), executor.getComputingTime());
				break;
			}
			case NSGAII: { 
				Algorithm<List<PlanejamentoSolucao>> algoritmo = new NSGAIIBuilder<PlanejamentoSolucao>(problema, solucaoPlanejamento, operacaoMutacao)
					.setMaxIterations(parametros.getQtdeInteracao())
					.setPopulationSize(parametros.getTamPopulacao())
					.build();
				AlgorithmRunner executor = new AlgorithmRunner.Executor(algoritmo).execute();
				resultado = new ResultadoExecucao(FiltroPopulacao.getMelhorSolucao(algoritmo.getResult()), executor.getComputingTime());
				break;
			}
			case PESA2: {
				Algorithm<List<PlanejamentoSolucao>> algoritmo = new PESA2Builder<PlanejamentoSolucao>(problema, solucaoPlanejamento, operacaoMutacao)
					.setMaxEvaluations(parametros.getQtdeInteracao() * parametros.getTamPopulacao())
					.setPopulationSize(parametros.getTamPopulacao())
					.build();
				AlgorithmRunner executor = new AlgorithmRunner.Executor(algoritmo).execute();
				resultado = new ResultadoExecucao(FiltroPopulacao.getMelhorSolucao(algoritmo.getResult()), executor.getComputingTime());
				break;
			}
//			case SMSEMOA: {
//				Algorithm<List<PlanejamentoSolucao>> algorithm = new SMSEMOABuilder<PlanejamentoSolucao>(problema, solucaoPlanejamento, operacaoMutacao)
//					.setMaxEvaluations(parametros.getQtdeInteracao())
//					.setPopulationSize(parametros.getTamPopulacao())
//					.build();
//				new AlgorithmRunner.Executor(algorithm).execute();
//				populacao = algorithm.getResult();
//				break;
//			}
			case SPEA2: {
				Algorithm<List<PlanejamentoSolucao>> algoritmo = new SPEA2Builder<PlanejamentoSolucao>(problema, solucaoPlanejamento, operacaoMutacao)
					.setMaxIterations(parametros.getQtdeInteracao())
					.setPopulationSize(parametros.getTamPopulacao())
					.build();
				AlgorithmRunner executor = new AlgorithmRunner.Executor(algoritmo).execute();
				resultado = new ResultadoExecucao(FiltroPopulacao.getMelhorSolucao(algoritmo.getResult()), executor.getComputingTime());
				break;
			}
//			case STEADY: {
//				Algorithm<PlanejamentoSolucao> algorithm = new GeneticAlgorithmBuilder<PlanejamentoSolucao>(problema, solucaoPlanejamento, operacaoMutacao)
//						.setMaxEvaluations(parametros.getTamPopulacao()*parametros.getQtdeInteracao())
//						.setPopulationSize(parametros.getTamPopulacao())
//						.setVariant(GeneticAlgorithmBuilder.GeneticAlgorithmVariant.STEADY_STATE)
//						.build();
//				new AlgorithmRunner.Executor(algorithm).execute();
//				populacao = new ArrayList<>();
//				populacao.add(algorithm.getResult());
//				break;
//			}
			default:
				System.err.println("Algoritmo não implementado");
				break;
		}
		
		return resultado;
	}

}