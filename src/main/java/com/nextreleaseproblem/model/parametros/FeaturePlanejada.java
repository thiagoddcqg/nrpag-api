package com.nextreleaseproblem.model.parametros;

/**
 * Descreve um recurso em um planejamento
 * Contém
 * - o recurso a ser feito
 * - o funcionário responsável pelo recurso
 * - a hora de início no planejamento
 * - a hora final no planejamento
  */
public class FeaturePlanejada {

	/**
	 * A hora de início do recurso planejado
	 */
	private double horaInicio;

	/**
	 *O funcionário que fará o recurso
	 */
	private Funcionario funcionario;

	/**
	 * A hora final do recurso planejado
	 */
	private double horaFim;
	
	/**
	 * Feature
	 */
	private Feature feature;
	
	
	/**
	 * @return hora inicial
	 */
	public double getHoraInicio() {
		return horaInicio;
	}

	/**
	 * @param horaInicio a hora inicial definida
	 */
	public void setHoraInicio(double horaInicio) {
		this.horaInicio = horaInicio;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public double getHoraFim() {
		return horaFim;
	}

	public void setHoraFim(double horaFim) {
		this.horaFim = horaFim;
	}

	public Feature getFeature() {
		return feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}


	/**
	 * Construa um recurso planejado
	 * @param feature o recurso para planejar
	 * @param funcionario o funcionário que realizou o recurso
	 */
	public FeaturePlanejada(Feature feature, Funcionario funcionario) {
		this.feature = feature;
		this.funcionario = funcionario;
		horaInicio = 0.0;
	}
	
	public FeaturePlanejada(FeaturePlanejada origin) {
		this.funcionario = origin.getFuncionario();
		this.horaInicio = origin.getHoraInicio();
		this.feature = origin.getFeature();
		this.horaFim = origin.getHoraFim();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (getClass() != obj.getClass())
			return false;

		FeaturePlanejada other = (FeaturePlanejada) obj;

		return other.getFeature().equals(this.getFeature()) &&
				other.getFuncionario().equals(this.getFuncionario()) &&
				other.getHoraInicio() == this.getHoraInicio() &&
				other.getHoraFim() == this.getHoraFim();
	}
	
	@Override
	public int hashCode() {
		return Double.valueOf(getHoraInicio()).intValue();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(getFeature()).append(" feito por ").append(getFuncionario())
			.append(" de ").append(getHoraInicio()).append(" até ").append(getHoraFim());
		
		return sb.toString();
	}
}
