/**
 * 
 */
package com.nextreleaseproblem.model.parametros;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Encapsula os dados de um problema de próximo lançamento
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DADOS_PROBLEMA")
public class DadosProblema {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(mappedBy = "dadosProblema", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Feature> features;

	@OneToMany(mappedBy = "dadosProblema", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Funcionario> funcionarios;

	@OneToMany(mappedBy = "dadosProblema", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Habilidade> habilidades;

	public DadosProblema(List<Feature> features, List<Funcionario> funcionarios, List<Habilidade> habilidades) {
		this.features = features;
		this.funcionarios = funcionarios;
		this.habilidades = habilidades;
	}

}
