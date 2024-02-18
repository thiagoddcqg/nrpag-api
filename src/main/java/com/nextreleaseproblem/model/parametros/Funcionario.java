package com.nextreleaseproblem.model.parametros;

import java.util.ArrayList;
import java.util.List;

/**
 * Descreve um funcionário que pode realizar um recurso
 */
public class Funcionario {
	
	/**
	 * Nome do empregado
	 */
	private String nome;
	
	/**
	 * As abilidades do empregado
	 */
	private List<Habilidade> habilidades;

	/**
	 * O número disponível de horas por semana
	 */
	private double disponibilidadeSemanal;

	

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Habilidade> getAbilidades() {
		return habilidades;
	}

	public void setAbilidades(List<Habilidade> habilidades) {
		this.habilidades = habilidades;
	}

	public double getDisponibilidadeSemanal() {
		return disponibilidadeSemanal;
	}

	public void setDisponibilidadeSemanal(double disponibilidadeSemanal) {
		this.disponibilidadeSemanal = disponibilidadeSemanal;
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