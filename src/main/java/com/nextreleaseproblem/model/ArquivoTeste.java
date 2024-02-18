package com.nextreleaseproblem.model;

public enum ArquivoTeste {
	MAIS_SIMPLES("simplest"),
	OTIMIZACAO_SIMPLES("simple_optimisation"),
	PRECEDENCIA("precedence"),
	PRECEDENCIAS("precedences"),
	TRANSBORDAR("overflow"),
	OTIMIZACAO_EXCESSO("overflow_optimisation"),
	EXCESSO_DE_FUNCIONARIOS("employee_overflow"),
	GERADO("generated"),
	HABILIDADES("skills");
	
	private String nomeArquivo;
	
	public String getFeaturesFileName() {
		return nomeArquivo + ".features";
	}
	
	public String getEmployeesFileName() {
		return nomeArquivo + ".employees";
	}
	
	private ArquivoTeste(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

}
