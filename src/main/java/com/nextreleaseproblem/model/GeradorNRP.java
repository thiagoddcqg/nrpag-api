package com.nextreleaseproblem.model;

import com.nextreleaseproblem.model.parametros.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeradorNRP {
	/**
	 * Gerar dados para um próximo problema de lançamento
	 * @param parametros os parâmetros do gerador
	 * @return os dados gerados
	 */
	public static DadosProblema gerar(GeradorParametros parametros) {
		List<Habilidade> habilidades = gerarHabilidades(parametros.getNumeroDeHabilidades());
		List<Feature> features = gerarFeatures(parametros.getNumeroDeFeatures(), parametros.getTaxaDeRestricoesDePrecedencia(), habilidades);
		List<Funcionario> funcionarios = gerarEmpregados(parametros.getNumeroDeEmpregados(), habilidades);
		
		return new DadosProblema(features, funcionarios, habilidades);
	}

	/**
	 * Gere habilidades numeroDehabilidades em uma lista
	 * @param numeroDehabilidades o número de habilidades a serem geradas
	 * @return a lista contendo as habilidades
	 */
	private static List<Habilidade> gerarHabilidades(int numeroDehabilidades) {
		List<Habilidade> habilidades = new ArrayList<>(numeroDehabilidades);
		
		for (int i = 1 ; i <= numeroDehabilidades ; i++) {
			habilidades.add(new Habilidade("Skill " + i));
		}
		
		return habilidades;
	}

	/**
	 * Gera nova lista de novos recursos
	 * @param numeroDeFeatures o número de recursos a serem gerados
	 * @param taxaDePrecedencia a restrição de taxa de precedência
	 * @param habilidades as habilidades disponíveis
	 * @return a lista dos novos recursos gerados
	 */
	private static List<Feature> gerarFeatures(int numeroDeFeatures, double taxaDePrecedencia, List<Habilidade> habilidades) {
		Random geradorAleatorio = new Random();
		NivelPrioridadeEnum[] prioridades = NivelPrioridadeEnum.values();
		int parametroFeaturesAnteriores = Double.valueOf(numeroDeFeatures * taxaDePrecedencia).intValue();
		List<Feature> features = new ArrayList<>(numeroDeFeatures);
		
		for (int i = 0 ; i < numeroDeFeatures ; i++) {
			List<Feature> featuresAnteriores = new ArrayList<>();
			if (features.size() > 0 && parametroFeaturesAnteriores > 0) {
				double probabilidade = parametroFeaturesAnteriores/(1.0*numeroDeFeatures-i);
				List<Feature> possiveisFeaturesAnteriores = new ArrayList<>(features);
				while (parametroFeaturesAnteriores > 0 && possiveisFeaturesAnteriores.size() > 0 && geradorAleatorio.nextDouble() < probabilidade) {
					int indiceFeature = geradorAleatorio.nextInt(possiveisFeaturesAnteriores.size());
					featuresAnteriores.add(possiveisFeaturesAnteriores.get(indiceFeature));
					possiveisFeaturesAnteriores.remove(indiceFeature);
					parametroFeaturesAnteriores--;
					probabilidade = parametroFeaturesAnteriores/(numeroDeFeatures-i);
				}
			}
			
			List<Habilidade> habilidadesRequeridas = new ArrayList<>(1);
			habilidadesRequeridas.add(habilidades.get(geradorAleatorio.nextInt(habilidades.size())));
			
			features.add(new Feature("Feature " + i,
					prioridades[geradorAleatorio.nextInt(prioridades.length)],
					1.0 * (1 + geradorAleatorio.nextInt(Double.valueOf(GeradorParametrosPadrao.MAXIMA_DURACAO_FEATURES).intValue())),
					featuresAnteriores,
					habilidadesRequeridas));
		}
		
		return features;
	}

	/**
	 *	Gera novos funcionários
	 * 	@param numeroDeEmpregados o número de funcionários a serem gerados
	 * 	@param habilidades as habilidades disponíveis
	 * 	@return a lista de novos funcionários
	 */
	private static List<Funcionario> gerarEmpregados(int numeroDeEmpregados, List<Habilidade> habilidades) {
		Random geradorAleatorio = new Random();
		List<Funcionario> funcionarios = new ArrayList<>(numeroDeEmpregados);
		int maximoEmpregadoDisponibilidadeSemana = Double.valueOf(GeradorParametrosPadrao.MAXIMO_EMPREGADO_DISPONIBILIDADE_SEMANA - GeradorParametrosPadrao.MINIMO_EMPREGADO_DISPONIBILIDADE_SEMANA + 1).intValue();
		
		for (int i = 1 ; i <= numeroDeEmpregados ; i ++) {
			int numeroDeHabilidades = 1 + geradorAleatorio.nextInt(habilidades.size());
			List<Habilidade> habilidadesEmpregado = new ArrayList<>(numeroDeHabilidades);
			List<Habilidade> habilidadesDisponiveis = new ArrayList<>(habilidades);
			for (int j = 0 ; j < numeroDeHabilidades ; j++) {
				int indiceHabilidade = geradorAleatorio.nextInt(habilidadesDisponiveis.size());
				habilidadesEmpregado.add(habilidadesDisponiveis.get(indiceHabilidade));
				habilidadesDisponiveis.remove(indiceHabilidade);
			}
			funcionarios.add(new Funcionario("Empregado " + i,
					1.0 * (GeradorParametrosPadrao.MINIMO_EMPREGADO_DISPONIBILIDADE_SEMANA + geradorAleatorio.nextInt(maximoEmpregadoDisponibilidadeSemana)),
					habilidadesEmpregado));
		}
		
		return funcionarios;
	}

	/**
	 * Grava os dados em arquivos gerados.features e gerados.employees
	 * @param dados os dados a serem gravados
	 */
	public static void gravarArquivo(DadosProblema dados) {
		File arquivoFeatures = new File("..\\metaheristicas\\src\\main\\java\\com\\nextreleaseproblem\\test\\inputs\\generated.features");
		File arquivoEmpregados = new File("..\\metaheristicas\\src\\main\\java\\com\\nextreleaseproblem\\test\\inputs\\generated.employees");
		FileWriter fileWriter;

		try {
			fileWriter = new FileWriter(arquivoFeatures);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(getFeaturesTexto(dados.getFeatures()));
			bufferedWriter.close();
			
			fileWriter = new FileWriter(arquivoEmpregados);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(getEmpregadoTexto(dados.getFuncionarios()));
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retorna todas as linhas de recursos em formato de string
	 * @param features os recursos para escrever
	 * @return os recursos em formato de arquivo string
	 */
	private static String getFeaturesTexto(List<Feature> features) {
		StringBuilder stringBuilder = new StringBuilder();
		
		for (Feature feature : features) {
			stringBuilder.append(feature.getNome()).append('\t')
				.append(feature.getPrioridade().getNivel()).append('\t')
				.append(feature.getDuracao()).append('\t')
				.append(feature.getHabilidadesRequeridas().get(0).getNome()).append('\t');
			
			for (Feature featureAnterior : feature.getFeatuesAnteriores()) {
				stringBuilder.append(featureAnterior.getNome()).append(',');
			}
			if (feature.getFeatuesAnteriores().size() > 0) {
				stringBuilder.deleteCharAt(stringBuilder.length() - 1);
			}
			
			stringBuilder.append('\n');
		}
		stringBuilder.deleteCharAt(stringBuilder.length() - 1); // Deletes the last '\n'
		
		return stringBuilder.toString();
	}

	/**
	 * Devolva os funcionários para escrever em um arquivo de dados
	 * @param funcionarios os funcionários escreverão
	 * @return a string que representa os funcionários
	 */
	private static String getEmpregadoTexto(List<Funcionario> funcionarios) {
		StringBuilder stringBuilder = new StringBuilder();
		
		for (Funcionario funcionario : funcionarios) {
			stringBuilder.append(funcionario.getNome()).append('\t')
				.append(funcionario.getDisponibilidadeSemanal()).append('\t');
			
			for (Habilidade habilidade : funcionario.getAbilidades()) {
				stringBuilder.append(habilidade.getNome()).append(',');
			}
			stringBuilder.deleteCharAt(stringBuilder.length() - 1); // Deleta o último ','
			
			stringBuilder.append('\n');
		}
		stringBuilder.deleteCharAt(stringBuilder.length() - 1); // deleta o último '\n'
		
		return stringBuilder.toString();
	}
}
