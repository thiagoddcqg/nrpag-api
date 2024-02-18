package com.nextreleaseproblem.model.parametros;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe representando uma característica do Problema do Próximo Lançamento
 */
public class Feature {

	/**
	 * nome da feature
	 */
	private String nome;
	
	/**
	 * a prioridade da feature
	 */
	private NivelPrioridadeEnum prioridade;
	
	/**
	 * a duração da feature em horas
	 */
	private double duracao;
	
	/**
	 * As features que precisavam ser executados antes
	 */
	private List<Feature> featuesAnteriores;
	
	/**
	 * as habilidades requeridas pelas feature
	 */
	private List<Habilidade> habilidadesRequeridas;
	
	public String getNome() {
		return nome;
	}

	public NivelPrioridadeEnum getPrioridade() {
		return prioridade;
	}

	public double getDuracao() {
		return duracao;
	}

	public List<Feature> getFeatuesAnteriores() {
		return featuesAnteriores;
	}
	
	public List<Habilidade> getHabilidadesRequeridas() {
		return habilidadesRequeridas;
	}


	/**
	 * Construtor da Feature
	 * @param nome o nome do recurso
	 * @param prioridade a prioridade do recurso
	 * @param duracao a duração do recurso
	 * @param featuesAnteriores a lista dos recursos anteriores ou null
	 * @param habilidadesRequeridas as habilidades necessárias para fazer esse recurso
	 */
	public Feature(String nome, NivelPrioridadeEnum prioridade, Double duracao, List<Feature> featuesAnteriores, List<Habilidade> habilidadesRequeridas) {
		this.nome = nome;
		this.prioridade = prioridade;
		this.duracao = duracao;
		this.featuesAnteriores = featuesAnteriores == null ? new ArrayList<Feature>() : featuesAnteriores;
		this.habilidadesRequeridas = habilidadesRequeridas == null ? new ArrayList<Habilidade>() : habilidadesRequeridas;
	}

	/**
	 * Construtor com apenas uma habilidade
	 * @param nome do recurso
	 * @param prioridade prioridade do recurso
	 * @param duracao duração do recurso
	 * @param featuesAnteriores a lista dos recursos anteriores ou null
	 * @param habilidadeRequerida a habilidade necessária para fazer o recurso
	 */
	public Feature(String nome, NivelPrioridadeEnum prioridade, Double duracao, List<Feature> featuesAnteriores, Habilidade habilidadeRequerida) {
		this.nome = nome;
		this.prioridade = prioridade;
		this.duracao = duracao;
		this.featuesAnteriores = featuesAnteriores == null ? new ArrayList<Feature>() : featuesAnteriores;
		this.habilidadesRequeridas = new ArrayList<>();
		habilidadesRequeridas.add(habilidadeRequerida);
	}
	
	@Override
	public String toString() {
		return getNome();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (getClass() != obj.getClass())
			return false;

		Feature other = (Feature) obj;

		return other.getNome().equals(this.getNome());
	}
	
	@Override
	public int hashCode() {
		return getNome().length();
	}
}
