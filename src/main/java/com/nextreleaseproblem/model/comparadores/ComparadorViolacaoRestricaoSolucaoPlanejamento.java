package com.nextreleaseproblem.model.comparadores;


import com.nextreleaseproblem.model.PlanejamentoSolucao;
import org.uma.jmetal.util.comparator.ConstraintViolationComparator;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;

/**
 * Comparador de violação de restrições para soluções de planejamento
 */
public class ComparadorViolacaoRestricaoSolucaoPlanejamento implements ConstraintViolationComparator<PlanejamentoSolucao> {


	/**
	 * Número de atributos de restrição violados
	 */
	private NumberOfViolatedConstraints<PlanejamentoSolucao> numeroAtributoRestricaoViolada;


	/**
	 * Construtor
	 * Inicializa o número de atributos de restrições violadas
	 */
	public ComparadorViolacaoRestricaoSolucaoPlanejamento() {
		numeroAtributoRestricaoViolada = new NumberOfViolatedConstraints<>();
	}
	
	
	@Override
	public int compare(PlanejamentoSolucao solution1, PlanejamentoSolucao solution2) {
		int numViolatedConstraintInSol1 = numeroAtributoRestricaoViolada.getAttribute(solution1),
				numViolatedConstraintInSol2 = numeroAtributoRestricaoViolada.getAttribute(solution2);
		if (numViolatedConstraintInSol1 == numViolatedConstraintInSol2)
			return 0;
		else if (numViolatedConstraintInSol1 > numViolatedConstraintInSol2)
			return 1;
		else
			return -1;
	}

}
