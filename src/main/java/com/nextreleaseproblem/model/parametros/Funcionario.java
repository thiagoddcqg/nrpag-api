package com.nextreleaseproblem.model.parametros;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FUNCIONARIOS")
public class Funcionario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idFuncionario;
	
	/**
	 * Nome do empregado
	 */
	@Column(name = "NOME", nullable = false)
	private String nome;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id")
	private DadosProblema dadosProblema;
	
	/**
	 * As abilidades do empregado
	 */
	@Transient
	private List<Habilidade> habilidades;

	/**
	 * O número disponível de horas por semana
	 */
	private double disponibilidadeSemanal;

	public List<Habilidade> getAbilidades() {
		return habilidades;
	}

	/**
	 * Construtos de um novo empregado
	 * @param nome
	 * @param disponibilidadeSemanal em horas por semana
	 */
	public Funcionario(String nome, double disponibilidadeSemanal, List<Habilidade> habilidades) {
		this.nome = nome;
		this.disponibilidadeSemanal = disponibilidadeSemanal;
		this.habilidades = habilidades == null ? new ArrayList<>() : habilidades;
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

		Funcionario other = (Funcionario) obj;
		
		return this.getNome().equals(other.getNome());
	}
	
	@Override
	public int hashCode() {
		return getNome().length();
	}
}