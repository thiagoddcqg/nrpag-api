package com.nextreleaseproblem.model.parametros;

import lombok.Data;
import lombok.Getter;

@Getter
public enum AlgoritmoEnum {
//	GENERATIONAL("Generational GA"),
	MOCell("MOCell"),
	NSGAII("NSGA-II"),
	PESA2("PESA-II"),
//	SMSEMOA("SMSEMOA"), // Does not evaluate the constraints...
	SPEA2("SPEA-II"),
//	STEADY("Steady State GA") // The evaluator does not consider the constraints...
	;
	
	/**
	 * Nome do algoritmo
	 */
	private String nome;
	
	/**
	 * Construtor
	 * @param nome nome da escolha
	 */
	private AlgoritmoEnum(String nome) {
		this.nome = nome;
	}

	public static AlgoritmoEnum getByNome(String nome) {
		for (AlgoritmoEnum item : AlgoritmoEnum.values()) {
			if (item.getNome().equals(nome)) {
				return item;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return nome;
	}
}
