package com.nextreleaseproblem.model.parametros;

/**
 * Uma habilidade para executar uma tarefa
 * Possuído por funcionários
 */
public class Habilidade {
	

	/**
	 * Nome da habilidade
	 */
	private String nome;
	

	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		if (nome == null) {
			throw new NullPointerException();
		}
		this.nome = nome;
	}


	/**
	 * Cria uma uma habilidade
	 * @param nome o nome da habilidade a ser criada
	 */
	public Habilidade(String nome) {
		this.nome = nome;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (getClass() != obj.getClass())
			return false;

		Habilidade other = (Habilidade) obj;

		return other.getNome().equals(this.getNome());
	}
	
	@Override
	public int hashCode() {
		return getNome().length();
	}
	
	@Override
	public String toString() {
		return getNome();
	}
}
