package com.nextreleaseproblem.view;

import com.nextreleaseproblem.model.parametros.Funcionario;
import com.nextreleaseproblem.model.parametros.DisponibilidadeSemanalFuncionario;
import com.nextreleaseproblem.model.parametros.FeaturePlanejada;
import com.nextreleaseproblem.model.NextReleaseProblem;
import com.nextreleaseproblem.model.PlanejamentoSolucao;
import com.nextreleaseproblem.model.QualidadeSolucao;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HTMLEscrita implements Runnable {


	/**
	 * As soluções para exibir
	 */
	private List<PlanejamentoSolucao> solucoes;


	/**
	 * Construtor
	 * Prepara o problema e os atributos da solução para poder calcular o método run()
	 * @param solucoes As soluções do problema NRP
	 */
	public HTMLEscrita(List<PlanejamentoSolucao> solucoes) {
		this.solucoes = solucoes;
	}
	
	public HTMLEscrita(PlanejamentoSolucao solucaos) {
		this.solucoes = new ArrayList<>();
		solucoes.add(solucaos);
	}

	@Override
	public void run() {
		File htmlArquivo = new File("..\\metaheristicas\\src\\main\\java\\com\\nextreleaseproblem\\test\\output\\planning.html");
		FileWriter fileWriter;

		try {
			fileWriter = new FileWriter(htmlArquivo);
			BufferedWriter bufferW = new BufferedWriter(fileWriter);
			bufferW.write(getCodigoPaginaHTML());
			bufferW.close();
			Desktop.getDesktop().open(htmlArquivo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retorna o número da coluna necessária para exibir
	 * @param solucao a solução a ser exibida
	 * @return O número de colunas necessárias para exibir o planejamento
	 */
	private int getNumeroColunasNecessarias(PlanejamentoSolucao solucao) {
		return Double.valueOf(solucao.getDataFim()).intValue();
	}

	/**
	 * Retorne o conteúdo HTML da página a ser exibida
	 * @return a String contendo o código HTML a ser exibido
	 */
	public String getCodigoPaginaHTML() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("<!doctype html><html lang=\"en\"><head><meta charset=\"utf-8\">")
			.append("<title>").append("Planejando o próxima versão").append("</title>")
			.append(getEstilo()).append("</head><body>");
		
		int i = 1;
		for (PlanejamentoSolucao solution : solucoes) {
			builder.append("<h2>Solução ").append(i++).append("</h2>")
				.append(getTagTabelaSolucoes(solution));
		}
		
		builder.append("</body></html>");
		
		return builder.toString();
	}

	/**
	 * Retorna a tag da tabela de planejamento da solução
	 * @param solucao a solução a ser exibida
	 * @return A tag da tabela contendo todo o planejamento da solução
	 */
	private StringBuilder getTagTabelaSolucoes(PlanejamentoSolucao solucao) {
		StringBuilder stringBuilder = new StringBuilder();
		int numeroColunasTempo = getNumeroColunasNecessarias(solucao);
		
		if (solucao.getQtdFeaturesPlanejadas() == 0) {
			return stringBuilder.append("Não há nenhuma feature planejada nesta solução");
		}
		stringBuilder.append("<table><thead><tr><th></th>");
		
		// Head row of the planning table
		for (int i = 0 ; i < numeroColunasTempo ; ++i) {
			stringBuilder.append("<th>").append(i).append("h - ").append(i+1).append("h").append("</th>");
		}
		stringBuilder.append("</tr></thead><tbody>");

		// Linhas do funcionário da tabela de planejamento
		Map<Funcionario, List<DisponibilidadeSemanalFuncionario>> availabilities = solucao.getPlanejamentoFuncionarios();
		for (Funcionario funcionario : availabilities.keySet()) {
			stringBuilder.append(getTagLinhaEmpregado(funcionario, availabilities.get(funcionario), numeroColunasTempo));
		}
				
		stringBuilder.append("</tbody></table>");
		
		DecimalFormat decimalFormat = new DecimalFormat() ;
		decimalFormat.setMaximumFractionDigits ( 2 ) ;
		
		stringBuilder.append("<p>Data fim: ").append(solucao.getObjective(NextReleaseProblem.INDICE_DATA_FIM_OBJETIVO))
			.append("<br />Qualidade: ").append(decimalFormat.format(new QualidadeSolucao().getAttribute(solucao)*100)).append("%</p>");
		
		return stringBuilder;
	}
	
	private StringBuilder getTagLinhaEmpregado(Funcionario funcionario, List<DisponibilidadeSemanalFuncionario> planejamentos, int numeroColunasTempo) {
		StringBuilder stringBuilder = new StringBuilder("<tr>");
		
		stringBuilder.append("<td>").append(funcionario.getNome()).append("</td>");
		
		int i = 0;
		Iterator<DisponibilidadeSemanalFuncionario> disponibilidadeSemanaFuncionarioIterator = planejamentos.iterator();
		DisponibilidadeSemanalFuncionario planejamentoAtual = disponibilidadeSemanaFuncionarioIterator.hasNext() ? disponibilidadeSemanaFuncionarioIterator.next() : null;
		
		while (i < numeroColunasTempo) {
			
			if (planejamentoAtual == null || planejamentoAtual.getHoraInicio() != 1.0*i) { // If there is no more feature to display
				stringBuilder.append("<td></td>");
				i++;
			}
			else {
				stringBuilder.append(getTagColunaSemana(planejamentoAtual));
				i = Double.valueOf(planejamentoAtual.getHoraFim()).intValue();
				planejamentoAtual = disponibilidadeSemanaFuncionarioIterator.hasNext() ? disponibilidadeSemanaFuncionarioIterator.next() : null;
			}
		}
		
		stringBuilder.append("</tr>");
		
		return stringBuilder;
	}
	

	private StringBuilder getTagColunaSemana(DisponibilidadeSemanalFuncionario planejamentoSemanal) {
		StringBuilder stringBuilder = new StringBuilder();
		double horaAtual = planejamentoSemanal.getHoraInicio();
		
		for (FeaturePlanejada featurePlanejada : planejamentoSemanal.getPlannedFeatures()) {
			while (horaAtual < featurePlanejada.getHoraInicio()) {
				stringBuilder.append("<td></td>");
				horaAtual += 1.0;
			}
			double colspan = Math.min(planejamentoSemanal.getHoraFim(), featurePlanejada.getHoraFim()) - horaAtual;
			stringBuilder.append("<td class=\"feature\" colspan=\"")
				.append(Double.valueOf(colspan).intValue())
				.append("\">").append(featurePlanejada.getFeature().getNome()).append("</td>");
			horaAtual = featurePlanejada.getHoraFim();
		}
		
		return stringBuilder;
	}

	/**
	 * Retorna a tag de estilo HTML para colocar no cabeçalho da página HTML
	 * @return a tag de estilo HTML com dados de estilo
	 */
	private StringBuilder getEstilo() {
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append("<style>")
			.append("table { width: auto; overflow-x: scroll; white-space: nowrap; border-collapse: collapse; } ")
			.append("table, th, td { border: 1px solid black; } ")
			.append("th, td { padding: 5px; } ")
			.append(".feature { background-color: #dfdfdf; }</style>");
		
		return stringBuilder;
	}
}
