/**
 * 
 */
package com.nextreleaseproblem.model.parametros;

/**
 * Esta classe encapsula os parâmetros de iteração
 */
public class ParametrosInteracao {

    /**
     * O número de semanas da iteração
     */
	private int numeroSemana;

    /**
     * O número de horas trabalhadas por semana
     */
	private double horasSemana;
	
	
	public int getNumeroSemana() {
		return numeroSemana;
	}

	public void setNumeroSemana(int numeroSemana) {
		this.numeroSemana = numeroSemana;
	}

	public double getHorasSemana() {
		return horasSemana;
	}

	public void setHorasSemana(double horasSemana) {
		this.horasSemana = horasSemana;
	}

    /**
     * Construtor padrão que constrói os parâmetros com seus valores padrão
     * Valores padrão da classe <code>ParametrosIteracaoPadrao</code>
     */
	public ParametrosInteracao() {
		this(ParametrosIteracaoPadrao.NUMERO_DA_SEMANA, ParametrosIteracaoPadrao.HORAS_DA_SEMANA);
	}

    /**
     * Construtor
     * @param numeroSemana O número da semana da iteração
     * @param horasSemana Número de horas trabalhadas por semana
     */
	public ParametrosInteracao(int numeroSemana, double horasSemana) {
		this.numeroSemana = numeroSemana;
		this.horasSemana = horasSemana;
	}

}
