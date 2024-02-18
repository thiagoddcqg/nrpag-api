package com.nextreleaseproblem.model.parametros;

/**
 * Os valores padrões do algoritmo
 */
public class PadraoAlgoritmoParametro {

	/**
	 * Probabilidade de cruzamento
	 */
	public final static double PPOBABILIDADE_CRUZAMENTO = 0.8;

	/**
	 * A proporção de soluções que não serão geradas de forma totalmente aleatória
	 */
	public final static double TAXA_GERACAO_NAO_GERADA_ALEATORIAMENTE = 0.1;

	/**
	 * Retorna o valor padrão da probabilidade de mutação considerando o número de features
	 * retornar 1.0/qtdeFeatures
	 * @param qtdeFeatures o número de featues do problema
	 * @return a probabilidade de mutação
	 */
	public final static double PROBABILIDADE_MUTACAO(int qtdeFeatures) {
		return 1.0/qtdeFeatures;
	}
}
