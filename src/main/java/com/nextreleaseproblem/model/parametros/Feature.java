package com.nextreleaseproblem.model.parametros;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FEATURES")
public class Feature {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idFeature;

	/**
	 * nome da feature
	 */
	@Column(name = "NOME", nullable = false)
	private String nome;
	
	/**
	 * a prioridade da feature
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "PRIORIDADE", nullable = false)
	private NivelPrioridadeEnum prioridade;
	
	/**
	 * a duração da feature em horas
	 */
	@Column(name = "DURACAO", nullable = false)
	private double duracao;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id")
	private DadosProblema dadosProblema;
	
	/**
	 * As features que precisavam ser executados antes
	 */
	@Transient
	private List<Feature> featuesAnteriores;
	
	/**
	 * as habilidades requeridas pelas feature
	 */
	@Transient
	private List<Habilidade> habilidadesRequeridas;
	
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
