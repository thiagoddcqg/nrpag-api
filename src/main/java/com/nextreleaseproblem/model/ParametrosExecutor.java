package com.nextreleaseproblem.model;


import com.nextreleaseproblem.model.parametros.ParametrosPadrao;

public class ParametrosExecutor {

	/**
	 * O número de iterações
	 */
	private int qtdeInteracao;

	/**
	 * O tamanho da população
	 */
	private int tamPopulacao;


	public int getQtdeInteracao() {
		return qtdeInteracao;
	}

	public void setQtdeInteracao(int qtdeInteracao) {
		this.qtdeInteracao = qtdeInteracao;
	}

	public int getTamPopulacao() {
		return tamPopulacao;
	}

	public void setTamPopulacao(int tamPopulacao) {
		this.tamPopulacao = tamPopulacao;
	}


	/**
	 * Construtor inicializando os atributos com valores padrão
	 */
	public ParametrosExecutor() {
		this.qtdeInteracao = ParametrosPadrao.NUMBER_OF_ITERATIONS;
		this.tamPopulacao = ParametrosPadrao.POPULATION_SIZE;
	}

	/**
	 * Construa os parâmetros do executor
	 * @param qtdeInteracao o número de iterações a serem executadas
	 * @param tamPopulacao o tamanho da população
	 */
	public ParametrosExecutor(int qtdeInteracao, int tamPopulacao) {
		this.qtdeInteracao = qtdeInteracao;
		this.tamPopulacao = tamPopulacao;
	}

}
