package com.nextreleaseproblem.model.operadores;

import com.nextreleaseproblem.model.parametros.Feature;
import com.nextreleaseproblem.model.parametros.FeaturePlanejada;
import com.nextreleaseproblem.model.NextReleaseProblem;
import com.nextreleaseproblem.model.PlanejamentoSolucao;

import java.util.Iterator;

/**
 * Fixador para soluções com restrições violadas
 */
public class ReparaOperador {

	/**
	 * O problema a resolver
	 */
	private NextReleaseProblem problema;

	/**
	 * Construtor do reparador
	 * @param problema problema para resolver
	 */
	public ReparaOperador(NextReleaseProblem problema) {
		this.problema = problema;
	}

	/**
	 * Repare uma solução removendo os recursos que violaram as restrições
	 * @param solucao a solução para reparar
	 */
	public void reparar(PlanejamentoSolucao solucao) {
		Iterator<FeaturePlanejada> it = solucao.getFeaturesPlanejadas().iterator();
		problema.evaluate(solucao);
		
		while (it.hasNext()) {
			FeaturePlanejada featurePlanejada = it.next();
			boolean valido = true;
			Iterator<Feature> interatorAnterior = featurePlanejada.getFeature().getFeatuesAnteriores().iterator();
			
			while (valido && interatorAnterior.hasNext()) {
				Feature featureAnterior = interatorAnterior.next();
				FeaturePlanejada featurePlanejadaAnterior = solucao.encontreFeaturePlanejada(featureAnterior);
				if (featurePlanejadaAnterior == null || featurePlanejadaAnterior.getHoraFim() > featurePlanejada.getHoraInicio()) {
					solucao.cancelarAgendamento(featurePlanejada);
					it.remove();
					valido = false;
					problema.evaluate(solucao);
				}
			}
		}
	}
}
