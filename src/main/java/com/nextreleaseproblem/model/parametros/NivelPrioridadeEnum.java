package com.nextreleaseproblem.model.parametros;

/**
 * Nível de prioridade de uma feature
 */
public enum NivelPrioridadeEnum {
	UM(1, 160),
	DOIS(2, 80),
	TRES(3, 40),
	QUATRO(4, 20),
	CINCO(5, 10);
	
	/**
	 * Nivel de prioridade
	 */
	private int nivel;
	
	/**
	 * pontuação da prioridade
	 */
	private int pontuacao;
	

	public int getNivel() {
		return nivel;
	}

	public int getPontuacao() {
		return pontuacao;
	}

	/**
	 * Construtor
	 * @param nivel o nível de prioridade
	 * @param pontuacao a pontuação da prioridade
	 */
	private NivelPrioridadeEnum(int nivel, int pontuacao) {
		this.nivel = nivel;
		this.pontuacao = pontuacao;
	}

	/**
	 * Retorne o PriorityLevel de um nível
	 * @param nivel
	 * @return o PriorityLevel correspondente
	 */
	public static NivelPrioridadeEnum getPriorityByLevel(int nivel) {
		switch (nivel) {
			case 1:
				return NivelPrioridadeEnum.UM;
			case 2:
				return NivelPrioridadeEnum.DOIS;
			case 3:
				return NivelPrioridadeEnum.TRES;
			case 4:
				return NivelPrioridadeEnum.QUATRO;
			default:
				return NivelPrioridadeEnum.CINCO;
		}
	}
}
