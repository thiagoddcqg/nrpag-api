package com.nextreleaseproblem.model;


public class ResultadoExecucao {

	/**
	 * Resultado da solução
	 */
	private PlanejamentoSolucao solucao;
	
	/**
	 * Tempo de processamento
	 */
	private long tempoProcessamento;

	/**
	 * @return a solução
	 */
	public PlanejamentoSolucao getSolucao() {
		return solucao;
	}


	public void setSolucao(PlanejamentoSolucao solucao) {
		this.solucao = solucao;
	}


	public long getTempoProcessamento() {
		return tempoProcessamento;
	}


	public void setTempoProcessamento(long tempoProcessamento) {
		this.tempoProcessamento = tempoProcessamento;
	}
	
	public ResultadoExecucao(PlanejamentoSolucao Solucao, long tempoProcessamento) {
		this.solucao = Solucao;
		this.tempoProcessamento = tempoProcessamento;
	}
}
