package com.nextreleaseproblem.model.comparadores;


import com.nextreleaseproblem.model.NextReleaseProblem;
import com.nextreleaseproblem.model.PlanejamentoSolucao;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.ConstraintViolationComparator;

import java.util.Comparator;

/**
 * Comparador de solução de planejamento
 * Usa o Comparador de Violação de Restrição
 */
public class CompararadorSolucaoPlaneamentoViolacaoRestricao implements Comparator<PlanejamentoSolucao> {


	/**
	 * O comparador de restrições
	 */
	private ConstraintViolationComparator<PlanejamentoSolucao> comparadorViolacaoRestricao;

	/**
	 * Construtor
	 * Inicializa o comparador de restrições
	 */
	public CompararadorSolucaoPlaneamentoViolacaoRestricao() {
		comparadorViolacaoRestricao = new ComparadorViolacaoRestricaoSolucaoPlanejamento();
	}

	@Override
	public int compare(PlanejamentoSolucao solucao1, PlanejamentoSolucao solucao2) {
		if (solucao1 == null) {
			throw new JMetalException("Solução 1 é nula") ;
		} else if (solucao2 == null) {
			throw new JMetalException("Solução 2 é nula") ;
		} else if (solucao1.getNumberOfObjectives() != solucao2.getNumberOfObjectives()) {
			throw new JMetalException("Não é possível comparar porque a solução1 tem " +
					solucao1.getNumberOfObjectives()+ " objetivos e solução2 tem " +
					solucao2.getNumberOfObjectives()) ;
		}
		
		int resultado = comparadorViolacaoRestricao.compare(solucao1, solucao2) ;
		if (resultado == 0) {
			resultado = testeDominancia(solucao1, solucao2) ;
		}

		return resultado;
	}

	/**
	 * Compara os objetivos das duas soluções
	 * Retorna o resultado da comparação do objetivo de pontuação prioritária
	 * Se forem iguais, compara o objetivo da data final
	 * @param solucao1 a primeira solução
	 * @param solucao2 a segunda solução
	 * @return -1 se a solucao1 for melhor que a segunda, 0 se forem iguais e 1 se a segunda solução for melhor que a primeira
	 */
	private int testeDominancia(PlanejamentoSolucao solucao1, PlanejamentoSolucao solucao2) {
		final int INDICE_PRIORIDADE_OBJETIVO = NextReleaseProblem.INDICE_PRIORIDADE_OBJETIVO;
		double sol1ValorObjetivoPrioridade = solucao1.getObjective(INDICE_PRIORIDADE_OBJETIVO);
		double sol2ValorObjetivoPrioridade = solucao2.getObjective(INDICE_PRIORIDADE_OBJETIVO);

		if (sol1ValorObjetivoPrioridade < sol2ValorObjetivoPrioridade) {
			return -1;
		}
		else if (sol1ValorObjetivoPrioridade > sol2ValorObjetivoPrioridade) {
			return 1;
		}
		else {
			final int INDICE_DATA_FIM_OBJETIVO = NextReleaseProblem.INDICE_DATA_FIM_OBJETIVO;
			double sol1ValorDataFimObjetivo = solucao1.getObjective(INDICE_DATA_FIM_OBJETIVO);
			double sol2ValorDataFimObjetivo = solucao2.getObjective(INDICE_DATA_FIM_OBJETIVO);
			if (sol1ValorDataFimObjetivo < sol2ValorDataFimObjetivo) {
				return -1;
			}
			else if (sol1ValorDataFimObjetivo > sol2ValorDataFimObjetivo) {
				return 1;
			}
			else {
				return 0;
			}
		}
	}

}
