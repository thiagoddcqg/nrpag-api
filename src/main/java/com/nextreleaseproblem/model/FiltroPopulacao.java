package com.nextreleaseproblem.model;



import com.nextreleaseproblem.model.comparadores.CompararadorSolucaoPlaneamentoViolacaoRestricao;

import java.util.*;

public class FiltroPopulacao {

	/**
	 * Classifica a população por dominância
	 * usa um <code>PlaningSolutionDominanceComparator</code>
	 * @param populacao a populaçao a ser ordenada
	 */
	private static void ordernarPorDominancia(List<PlanejamentoSolucao> populacao) {
		Collections.sort(populacao, new CompararadorSolucaoPlaneamentoViolacaoRestricao());
	}

	/**
	 * Retorna a melhor solução após ordenar por dominância, retorna a primeira
	 * @param populacao a população base
	 * @return a melhor solução
	 */
	public static PlanejamentoSolucao getMelhorSolucao(List<PlanejamentoSolucao> populacao) {
		ordernarPorDominancia(populacao);
		return populacao.get(0);
	}

	/**
	 * Retorna apenas as melhores soluções
	 * Usa o <code>PlanningSolutionDominanceComparator</code>
	 * @param population A população apenas com as melhores soluções
	 */
	public static Set<PlanejamentoSolucao> getMelhoresSolucoes(List<PlanejamentoSolucao> population) {
		Set<PlanejamentoSolucao> melhorSolucoes = new HashSet<PlanejamentoSolucao>();
		
		if (population.size() == 0) {
			return melhorSolucoes;
		}
		
		PlanejamentoSolucao melhorSolucao = getMelhorSolucao(population);
		
		Comparator<PlanejamentoSolucao> comparator = new CompararadorSolucaoPlaneamentoViolacaoRestricao();
		Iterator<PlanejamentoSolucao> iterator = population.iterator();
		
		while (iterator.hasNext()) {
			PlanejamentoSolucao currentSolution = (PlanejamentoSolucao) iterator.next();
			if (comparator.compare(currentSolution, melhorSolucao) == 0) {
				melhorSolucoes.add(currentSolution);
			}
		}
		
		return melhorSolucoes;
	}
}
