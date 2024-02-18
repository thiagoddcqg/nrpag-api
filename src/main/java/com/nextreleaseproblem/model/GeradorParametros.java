package com.nextreleaseproblem.model;

public class GeradorParametros {

	/**
	 * O número de features a serem gerados
	 */
	private int numeroDeFeatures;

	/**
	 * O número de funcionários a gerar
	 */
	private int numeroDeEmpregados;

	/**
	 * O número de habilidades usadas
	 */
	private int numeroDeHabilidades;

	/**
	 * A taxa de features com restrições de precedência
	 */
	private double taxaDeRestricoesDePrecedencia;

	
	public int getNumeroDeFeatures() {
		return numeroDeFeatures;
	}

	public void setNumeroDeFeatures(int numeroDeFeatures) {
		this.numeroDeFeatures = numeroDeFeatures;
	}

	public int getNumeroDeEmpregados() {
		return numeroDeEmpregados;
	}

	public void setNumeroDeEmpregados(int numeroDeEmpregados) {
		this.numeroDeEmpregados = numeroDeEmpregados;
	}

	public int getNumeroDeHabilidades() {
		return numeroDeHabilidades;
	}

	public void setNumeroDeHabilidades(int numeroDeHabilidades) {
		this.numeroDeHabilidades = numeroDeHabilidades;
	}

	public double getTaxaDeRestricoesDePrecedencia() {
		return taxaDeRestricoesDePrecedencia;
	}

	public void setTaxaDeRestricoesDePrecedencia(double taxaDeRestricoesDePrecedencia) {
		this.taxaDeRestricoesDePrecedencia = taxaDeRestricoesDePrecedencia;
	}

	/**
	 * Construtores padrão que inicializam os parâmetros com seus valores padrão
	 * Os valores padrão podem ser encontrados na classe {@link GeradorParametrosPadrao}
	 */
	public GeradorParametros() {
		this(GeradorParametrosPadrao.NUMERO_DE_FEATURES,
				GeradorParametrosPadrao.NUMERO_DE_EMPREGADOS,
				GeradorParametrosPadrao.NUMERO_DE_HABILIDADES,
				GeradorParametrosPadrao.TAXA_DE_PRECEDENCIA);
	}
	
	/**
	 * @param numeroDeFeatures
	 * @param numeroDeEmpregados
	 * @param numeroDeHabilidades
	 * @param taxaDeRestricoesDePrecedencia
	 */
	public GeradorParametros(int numeroDeFeatures, int numeroDeEmpregados, int numeroDeHabilidades,
							 double taxaDeRestricoesDePrecedencia) {
		this.numeroDeFeatures = numeroDeFeatures;
		this.numeroDeEmpregados = numeroDeEmpregados;
		this.numeroDeHabilidades = numeroDeHabilidades;
		this.taxaDeRestricoesDePrecedencia = taxaDeRestricoesDePrecedencia;
	}
}
