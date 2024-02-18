/**
 * 
 */
package com.nextreleaseproblem.model.operadores;

import com.nextreleaseproblem.model.parametros.PadraoAlgoritmoParametro;
import com.nextreleaseproblem.model.parametros.FeaturePlanejada;
import com.nextreleaseproblem.model.NextReleaseProblem;
import com.nextreleaseproblem.model.PlanejamentoSolucao;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * O operador de cruzamento na Solução de Planejamento
 */
public class OperadorCruzamentoPlanejamento implements CrossoverOperator<PlanejamentoSolucao> {

	/**
	 * Probabilidade de cruzamento, entre 0.0 e 1.0
	 */
	private double probabilidadeCruzamento;

	/**
	 * Gerador randomico
	 */
	private JMetalRandom geradorAleatorio;
	
	/**
	 * Problema da próxima versão
	 */
	private NextReleaseProblem problema;

	/**
	 *	Construtor que inicializa a probabilidade de cruzamento com seu valor padrão
	 *	Valor padrão de {@link PadraoAlgoritmoParametro}
	 * 	@param problema
	 */
	public OperadorCruzamentoPlanejamento(NextReleaseProblem problema) {
		this(problema, PadraoAlgoritmoParametro.PPOBABILIDADE_CRUZAMENTO);
	}

	/**
	 * Constructor
	 * @param problema Problema da próxima versão
	 * @param probabilidadeCruzamento a probabilidade de cruzamento, entre 0.0 e 1.0
	 */
	public OperadorCruzamentoPlanejamento(NextReleaseProblem problema, double probabilidadeCruzamento) {
		if (probabilidadeCruzamento < 0) {
			throw new JMetalException("A probabilidade de cruzamento é negativa: " + probabilidadeCruzamento) ;
		}

		this.probabilidadeCruzamento = probabilidadeCruzamento;
		this.problema = problema;
		geradorAleatorio = JMetalRandom.getInstance() ;
	}


	@Override
	public List<PlanejamentoSolucao> execute(List<PlanejamentoSolucao> solucoes) {
		if (null == solucoes) {
			throw new JMetalException("parâmetro nulo") ;
		} else if (solucoes.size() != 2) {
			throw new JMetalException("Deve haver dois pais em vez de " + solucoes.size()) ;
		}

		return executarCruzamento(solucoes.get(0), solucoes.get(1)) ;
	}

	/**
	 * faça o cruzamento nas soluções de 2 parâmetros
	 * @param pai1 o primeiro pai
	 * @param pai2 o segundo pai
	 * @return uma lista com os dois filhos
	 */
	public List<PlanejamentoSolucao> executarCruzamento(PlanejamentoSolucao pai1, PlanejamentoSolucao pai2) {
		List<PlanejamentoSolucao> filhos = new ArrayList<PlanejamentoSolucao>(2);

		filhos.add((PlanejamentoSolucao) pai1.copy()) ;
		filhos.add((PlanejamentoSolucao) pai2.copy()) ;
		
		if (geradorAleatorio.nextDouble() < probabilidadeCruzamento) {
			PlanejamentoSolucao filho1 = filhos.get(0);
			PlanejamentoSolucao filho2 = filhos.get(1);
			
			int menorTamanho = Math.min(pai1.getQtdFeaturesPlanejadas(), pai2.getQtdFeaturesPlanejadas());
			
			if (menorTamanho > 0) {
				int posicaoCorte;
				
				if (menorTamanho == 1) {
					posicaoCorte = 1;
				} 
				else {
					posicaoCorte = geradorAleatorio.nextInt(1, menorTamanho);
				}
				
				List<FeaturePlanejada> fimFilho1 = filho2.getCopiaSublistaRecursosPlanejadosFinais(posicaoCorte);
				List<FeaturePlanejada> fimFilho2 = filho1.getCopiaSublistaRecursosPlanejadosFinais(posicaoCorte);
				for (FeaturePlanejada plannedTask : fimFilho2) {
					filho1.cancelarAgendamento(plannedTask);
				}
				for (FeaturePlanejada plannedTask : fimFilho1) {
					filho2.cancelarAgendamento(plannedTask);
				}


				//programa os novos fins e mantém na lista apenas se já estiverem planejados
				Iterator<FeaturePlanejada> iteratorFimFilho1 = fimFilho1.iterator();
				while (iteratorFimFilho1.hasNext()) {
					FeaturePlanejada plannedTask = (FeaturePlanejada) iteratorFimFilho1.next();
					if (!filho1.isPlanejada(plannedTask.getFeature())) {
						filho1.agendarFinal(plannedTask.getFeature(), plannedTask.getFuncionario());
						iteratorFimFilho1.remove();
					}
				}
				Iterator<FeaturePlanejada> iteratorFimFilho2 = fimFilho2.iterator();
				while (iteratorFimFilho2.hasNext()) {
					FeaturePlanejada tarefaPlanejada = (FeaturePlanejada) iteratorFimFilho2.next();
					if (!filho2.isPlanejada(tarefaPlanejada.getFeature())) {
						filho2.agendarFinal(tarefaPlanejada.getFeature(), tarefaPlanejada.getFuncionario());
						iteratorFimFilho2.remove();
					}
				}

				// Trocando as tarefas
				iteratorFimFilho1 = fimFilho1.iterator();
				iteratorFimFilho2 = fimFilho2.iterator();
				while (iteratorFimFilho1.hasNext() && iteratorFimFilho2.hasNext()) {
					FeaturePlanejada tarefa = iteratorFimFilho1.next();
					filho1.agendarFinal(tarefa.getFeature(), tarefa.getFuncionario());
					tarefa = iteratorFimFilho2.next();
					filho2.agendarFinal(tarefa.getFeature(), tarefa.getFuncionario());
				}
			}
		}
		
		return filhos;
	}
}
