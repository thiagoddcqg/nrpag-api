package com.nextreleaseproblem.model;

public enum NivelPrioridade {
	URGENTE(1, 270),
	ALTA(2, 90),
	MEDIA(3, 30),
	BAIXA(4, 10);
	
	private int nivel;
	
	private int score;
	
	public int getNivel() {
		return nivel;
	}

	public int getScore() {
		return score;
	}

	private NivelPrioridade(int nivel, int score) {
		this.nivel = nivel;
		this.score = score;
	}

	public static NivelPrioridade getNivelPrioridade(int level) {
		switch (level) {
			case 1:
				return NivelPrioridade.URGENTE;
			case 2:
				return NivelPrioridade.ALTA;
			case 3:
				return NivelPrioridade.MEDIA;
			case 4:
				return NivelPrioridade.BAIXA;
			default:
				return null;
		}
	}
}
