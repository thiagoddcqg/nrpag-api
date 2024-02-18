package com.nextreleaseproblem.model;

import com.nextreleaseproblem.model.parametros.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CarregadorDados {
	
	private final static int INDICE_NOME_FEATURE = 0;
	private final static int INDICE_PRIORIDADE_FEATURE = 1;
	private final static int INDICE_DRACAO_FEATURE = 2;
	private final static int INDICE_HABILIDADES_FEATURES = 3;
	private final static int INDICE_FEATURE_ANTERIOR = 4;
	private final static int INDICE_NOME_FUNCIONARIO = 0;
	private final static int INDICE_CLIENTE_DISPONIVEL = 1;
	private final static int INDICE_HABILIDADES_FUNCIONARIOS = 2;
	
	public final static String DIRETORIO_ENTRADA = new String("..\\metaheristicas\\src\\main\\java\\com\\nextreleaseproblem\\test\\inputs\\");

	public static DadosProblema lerDados(ArquivoTeste arquivo) {
		DadosProblema dado = null;
		
		try(BufferedReader featuresBufferedReader = new BufferedReader(new FileReader(DIRETORIO_ENTRADA + arquivo.getFeaturesFileName()));
			BufferedReader empregadosBufferedReader = new BufferedReader(new FileReader(DIRETORIO_ENTRADA + arquivo.getEmployeesFileName()))) {
			
			// Reading the features and skills
			List<Habilidade> habilidades = new ArrayList<>();
			List<Feature> features = new ArrayList<>();
			String linha = featuresBufferedReader.readLine();

			while (linha != null) {
				features.add(lerFeature(linha, habilidades, features));
				
				linha = featuresBufferedReader.readLine();
			}
			featuresBufferedReader.close();
			
			// lendo o empregado
			List<Funcionario> funcionarios = new ArrayList<>();
			linha = empregadosBufferedReader.readLine();

			while (linha != null) {
				funcionarios.add(lerEmpregado(linha, habilidades));
				
				linha = empregadosBufferedReader.readLine();
			}
			empregadosBufferedReader.close();
			
			dado = new DadosProblema(features, funcionarios, habilidades);
		} catch (FileNotFoundException e) {
			System.err.println("Arquivo de entrada não encontrado");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dado;
	}

	/**
	 * Lê uma linha de dados de funcionários
	 * Atualiza a lista de habilidades se houver novidades
	 * @param linha a linha a ser lida
	 * @param habilidades as habilidades já conhecidas
	 * @return o novo funcionário, ou null se a linha não estiver bem formada
	 */
	private static Funcionario lerEmpregado(String linha, List<Habilidade> habilidades) {
		String[] parts = linha.split("\\t");
		
		if (parts.length != 3)
			return null;
		
		Funcionario funcionario = new Funcionario(parts[INDICE_NOME_FUNCIONARIO],
								Double.valueOf(parts[INDICE_CLIENTE_DISPONIVEL]),
								lerHabilidades(habilidades, parts[INDICE_HABILIDADES_FUNCIONARIOS]));

		return funcionario;
	}

	/**
	 * Lê um recurso na linha e atualiza a lista de habilidades se houver um novo
	 * @param linha linha do arquivo de dados
	 * @param habilidades lista de habilidades já conhecidas
	 * @param features As funcionalidades já planejadas
	 * @return o novo recurso ou null se a linha não estiver bem formatada
	 */
	private static Feature lerFeature(String linha, List<Habilidade> habilidades, List<Feature> features) {
		String[] partes = linha.split("\\t");
		
		if (partes.length < 4)
			return null;
		
		List<Feature> featuresAnteriores = new ArrayList<>();
		if(partes.length == 5) {
			String nomesFeaturesAnteriores[] = partes[INDICE_FEATURE_ANTERIOR].split(",");
			for (String nomeFeatureAnterior : nomesFeaturesAnteriores) {
				featuresAnteriores.add(findFeature(features, nomeFeatureAnterior));
			}
		}
		
		Feature feature = new Feature(partes[INDICE_NOME_FEATURE],
						NivelPrioridadeEnum.getPriorityByLevel(Integer.valueOf(partes[INDICE_PRIORIDADE_FEATURE])),
						Double.valueOf(partes[INDICE_DRACAO_FEATURE]),
						featuresAnteriores,
						lerHabilidades(habilidades, partes[INDICE_HABILIDADES_FEATURES]));
		
		return feature;
	}
	
	private static List<Habilidade> lerHabilidades(List<Habilidade> habilidades, String listaHabilidades) {
		String habilidadesString[] = listaHabilidades.split(",");
		List<Habilidade> resultado = new ArrayList<>();
		for (String habilidadeString : habilidadesString) {
			Habilidade habilidade = encontrarHabilidade(habilidades, habilidadeString);
			if (habilidade == null) {
				habilidade = new Habilidade(habilidadeString);
				habilidades.add(habilidade);
			}
			resultado.add(habilidade);
		}
		return resultado;
	}

	/**
	 *	Encontre uma habilidade pelo nome na lista de habilidades
	 *	@param habilidades a lista de habilidades já conhecidas
	 * 	@param nomeHabilidade o nome da habilidade pesquisada
	 * 	@return a habilidade ou null se ainda não estiver na lista
	 */
	private static Habilidade encontrarHabilidade(List<Habilidade> habilidades, String nomeHabilidade) {
		for (Habilidade habilidade : habilidades) {
			if (habilidade.getNome().equals(nomeHabilidade)) {
				return habilidade;
			}
		}
		return null;
	}

	/**
	 * Encontre um recurso pelo nome na lista de recursos
	 * @param features a lista de recursos
	 * @param nome o nome do recurso a ser pesquisado
	 * @return o recurso correspondente ou null se não existir
	 */
	private static Feature findFeature(List<Feature> features, String nome) {
		Feature feature = null;
		int i = 0;
		
		while (feature == null && i < features.size()) {
			if (features.get(i).getNome().equals(nome)) {
				feature = features.get(i);
			}
			i++;
		}
		
		return feature;
	}
}
