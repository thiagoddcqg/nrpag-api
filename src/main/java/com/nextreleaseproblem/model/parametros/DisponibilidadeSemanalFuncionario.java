package com.nextreleaseproblem.model.parametros;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa o planejamento semanal de um funcionário
 */
public class DisponibilidadeSemanalFuncionario {

	/**
	 * O horário de início do funcionário na semana
	 */
	private double horaInicio;

	/**
	 * As horas restantes do funcionário
	 */
	private double horasRestantesDisponiveis;
	
	/**
	 * Hora final do funcionario na semana
	 */
	private double horaFim;
	
	/**
	 * features feitas durante a semana
	 */
	private List<FeaturePlanejada> featuresPlanejadas;
	
	

	public double getHorasRestantesDisponiveis() {
		return horasRestantesDisponiveis;
	}

	public void setHorasRestantesDisponiveis(double horasRestantesDisponiveis) {
		this.horasRestantesDisponiveis = horasRestantesDisponiveis;
	}

	public double getHoraFim() {
		return horaFim;
	}

	public void setHoraFim(double horaFim) {
		this.horaFim = horaFim;
	}

	public double getHoraInicio() {
		return horaInicio;
	}
	
	public List<FeaturePlanejada> getPlannedFeatures() {
		return featuresPlanejadas;
	}

	/**
	 * Adiciona uma featurePlanejada as featuresPlanejadas
	 * @param featurePlanejada
	 */
	public void addFeaturePlanejada(FeaturePlanejada featurePlanejada) {
		this.featuresPlanejadas.add(featurePlanejada);
	}

	
	/**
	 * Construtor
	 * @param horaInicio
	 * @param horasRestantesDisponiveis
	 */
	public DisponibilidadeSemanalFuncionario(double horaInicio, double horasRestantesDisponiveis) {
		this.horaInicio = horaInicio;
		this.horasRestantesDisponiveis = horasRestantesDisponiveis;
		horaFim = horaInicio;
		this.featuresPlanejadas = new ArrayList<>();
	}
	
	/**
	 * Construtor de cópia
	 * @param origem objeto a ser copiado
	 */
	public DisponibilidadeSemanalFuncionario(DisponibilidadeSemanalFuncionario origem) {
		this.horaInicio = origem.getHoraInicio();
		this.horasRestantesDisponiveis = origem.getHorasRestantesDisponiveis();
		this.horaFim = origem.getHoraFim();
		this.featuresPlanejadas = new ArrayList<>(origem.getPlannedFeatures().size());
		for (FeaturePlanejada featurePlanejada : origem.getPlannedFeatures()) {
			this.featuresPlanejadas.add(new FeaturePlanejada(featurePlanejada));
		}
	}
}
