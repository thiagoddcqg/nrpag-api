/**
 * 
 */
package com.nextreleaseproblem.model.parametros;

import java.util.List;

/**
 * Encapsula os dados de um problema de próximo lançamento
 */
public class DadosProblema {

	/**
	 * Lista de recursos
	 */
	private List<Feature> features;

	/**
	 * Lista de funcionários
	 */
	private List<Funcionario> funcionarios;

	/**
	 * Lista de habilidades
	 */
	private List<Habilidade> habilidades;

	
	public List<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(List<Feature> features) {
		this.features = features;
	}

	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(List<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}

	public List<Habilidade> getHabilidades() {
		return habilidades;
	}

	public void setHabilidades(List<Habilidade> habilidades) {
		this.habilidades = habilidades;
	}
	
	/**
	 * Constructor
	 * @param features the features of the problem
	 * @param funcionarios the employees of the problem
	 * @param habilidades the skills of the problem
	 */
	public DadosProblema(List<Feature> features, List<Funcionario> funcionarios, List<Habilidade> habilidades) {
		this.features = features;
		this.funcionarios = funcionarios;
		this.habilidades = habilidades;
	}

}
