package com.nextreleaseproblem.model.parametros;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "HABILIDADES")
public class Habilidade {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idHabilidade;

	/**
	 * Nome da habilidade
	 */
	@Column(name = "NOME", nullable = false)
	private String nome;


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id")
	private DadosProblema dadosProblema;


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
