package com.nextreleaseproblem.controller;

import com.nextreleaseproblem.model.ExecutorAlgoritmo;
import com.nextreleaseproblem.model.*;
import com.nextreleaseproblem.model.parametros.*;
import com.nextreleaseproblem.model.parametros.AlgoritmoEnum;
import com.nextreleaseproblem.model.GeradorNRP;
import com.nextreleaseproblem.repository.entity.AlgoritmoExecucao;
import com.nextreleaseproblem.util.GraphUtil;
import lombok.Data;
import org.jfree.chart.ChartFactory;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.JFreeChart;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.*;

@Data
public class ExperimentController {

	private List<String> resultados;
	private String linha = "";
	private int maxfuncionarios;
	private List<AlgoritmoExecucao> algoritmoExecucaoList;
	private List<AlgoritmoTempo> algoritmoTempoList;

	public ExperimentController(TipoExperimento experiment, int maxfuncionarios, int funcionariosiniciais, int numerofeatures, double avaliarhabilidadesfeatures, int reproducaoteste, int incrementofuncionarios, double taxaprecedencia, int featuresiniciais, int numeroempregados, int maximofeatures, int numerosemana, double horassemana, int incrementofeature, int tamanhomaximoproblema, int tamanhoinicial, int incrementotamanho) {
		resultados = new ArrayList<>();
		this.maxfuncionarios = maxfuncionarios;
		this.algoritmoExecucaoList = new ArrayList();
		algoritmoTempoList = new ArrayList<>();

		XYDataset ds;
		String xname;
		switch (experiment) {
		case EMPREGADOS:
			ds = executeEmployeesExperiment(maxfuncionarios, funcionariosiniciais, numerofeatures, avaliarhabilidadesfeatures, reproducaoteste, incrementofuncionarios, taxaprecedencia);
			xname = "Empregados";
			break;
		case FEATURES:
			ds = executeFeaturesExperiment(avaliarhabilidadesfeatures, reproducaoteste, taxaprecedencia, featuresiniciais, numeroempregados, maximofeatures, numerosemana, horassemana, incrementofeature);
			xname = "Features";
			break;
		default:
			ds = executeSizeExperiment(tamanhomaximoproblema, tamanhoinicial, reproducaoteste, incrementotamanho);
			xname = "Size";
			break;
		}
		JFreeChart chart = ChartFactory.createScatterPlot("Gráfico de Experimentação", xname, "Qualidade", ds);
		//JFreeChart chart = ChartFactory.createXYAreaChart("Gráfico de Experimentação", xname, "Qualidade", ds);

		resultados.add(GraphUtil.generateChartBase64(chart, 800, 600));
		resultados.add(linha);
		if(ds instanceof XYSeriesCollection){
			XYSeriesCollection dados = (XYSeriesCollection) ds;

            try {
                Field campo = XYSeriesCollection.class.getDeclaredField("data");
				campo.setAccessible(true);
				List<XYSeries> lista = (ArrayList<XYSeries>) campo.get(dados);
				for(XYSeries item : lista){
					AlgoritmoExecucao algoritmoExecucao = AlgoritmoExecucao.builder()
							.algoritmoEnum(AlgoritmoEnum.getByNome(item.getKey().toString()))
							.minimox(item.getMinX())
							.maximox(item.getMaxX())
							.minimoy(item.getMinY())
							.maximoy(item.getMaxY())
						.build();
					algoritmoExecucaoList.add(algoritmoExecucao);
				}
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

      //      algoritmoExecucao.setAlgoritmoEnum(AlgoritmoEnum.valueOf();
		}
		//algoritmoExecucao.setAlgoritmoEnum(AlgoritmoEnum.valueOf());
	}

	/**
	 * Execute an experiment raising the size
	 * @return the data of the experiment
	 */
	public XYDataset executeSizeExperiment(int tamanhomaximoproblema, int tamanhoinicial, int reproducaoteste, int incrementotamanho) {
		int size = tamanhoinicial;
		DadosProblema data;
		GeradorParametros generatorParams = getParameters(size);
		QualidadeSolucao qualityAttribute = new QualidadeSolucao();
		
		XYSeriesCollection dataset = initializeSeries();

		//String retorno = "";
		while (size <= tamanhomaximoproblema) {
			Map<AlgoritmoEnum, Double[]> qualityValues = new HashMap<>(AlgoritmoEnum.values().length);
			for (AlgoritmoEnum algorithm : AlgoritmoEnum.values()) {
				qualityValues.put(algorithm, new Double[reproducaoteste]);
			}
			
			for (int i = 0; i < reproducaoteste; i++) {
				data = GeradorNRP.gerar(generatorParams);
				NextReleaseProblem nrp = new NextReleaseProblem(data.getFeatures(), data.getFuncionarios(), new ParametrosInteracao(ParametrosIteracaoPadrao.NUMERO_DA_SEMANA, ParametrosIteracaoPadrao.HORAS_DA_SEMANA));
				ExecutorAlgoritmo executor = new ExecutorAlgoritmo(nrp, new ParametrosExecutor());

				for (AlgoritmoEnum algorithm : AlgoritmoEnum.values()) {
					printTrace(algorithm, size, i);
					ResultadoExecucao result = executor.executeAlgorithm(algorithm);
					Double[] values = qualityValues.get(algorithm);
					values[i] = qualityAttribute.getAttribute(result.getSolucao());
				}
			}
			
			for (AlgoritmoEnum algorithm : AlgoritmoEnum.values()) {
				int realSize = generatorParams.getNumeroDeEmpregados() + generatorParams.getNumeroDeFeatures();
				dataset.getSeries(algorithm.toString()).add(realSize, getAverage(qualityValues.get(algorithm)));
			}
			
			size += incrementotamanho;
			generatorParams = getParameters(size);
		}
		
		return dataset;
	}
	
	public XYDataset executeEmployeesExperiment(int maxfuncionarios, int funcionariosiniciais, int numerofeatures, double avaliarhabilidadesfeatures, int reproducaoteste, int incrementofuncionarios, double taxaprecedencia) {
		int numberOfEmployees = funcionariosiniciais;
		QualidadeSolucao qualityAttribute = new QualidadeSolucao();
		DadosProblema data;
		XYSeriesCollection dataset = initializeSeries();
		
		GeradorParametros generatorParams = new GeradorParametros(
				numerofeatures,
				numberOfEmployees, 
				(int) Math.round(numerofeatures * avaliarhabilidadesfeatures),
				taxaprecedencia);
		
		while (numberOfEmployees <= maxfuncionarios) {
			Map<AlgoritmoEnum, Double[]> qualityValues = new HashMap<>(AlgoritmoEnum.values().length);
			
			for (AlgoritmoEnum algorithm : AlgoritmoEnum.values()) {
				qualityValues.put(algorithm, new Double[reproducaoteste]);
			}
			
			for (int i = 0; i < reproducaoteste; i++) {
				data = GeradorNRP.gerar(generatorParams);
				NextReleaseProblem nrp = new NextReleaseProblem(data.getFeatures(), data.getFuncionarios(), new ParametrosInteracao(ParametrosIteracaoPadrao.NUMERO_DA_SEMANA, ParametrosIteracaoPadrao.HORAS_DA_SEMANA));
				ExecutorAlgoritmo executor = new ExecutorAlgoritmo(nrp, new ParametrosExecutor());
				
				for (AlgoritmoEnum algorithm : AlgoritmoEnum.values()) {
					printTrace(algorithm, numberOfEmployees, i);
					ResultadoExecucao result = executor.executeAlgorithm(algorithm);
					Double[] values = qualityValues.get(algorithm);
					values[i] = qualityAttribute.getAttribute(result.getSolucao());
				}
			}
			
			for (AlgoritmoEnum algorithm : AlgoritmoEnum.values()) {
				dataset.getSeries(algorithm.toString()).add(numberOfEmployees, getAverage(qualityValues.get(algorithm)));
			}
			
			numberOfEmployees += incrementofuncionarios;
			generatorParams.setNumeroDeEmpregados(numberOfEmployees);
		}
		
		return dataset;
	}
	
	public XYDataset executeFeaturesExperiment(double avaliarhabilidadesfeatures, int reproducaoteste, double taxaprecedencia, int featuresiniciais, int numeroempregados, int maximofeatures, int numerosemana, double horassemana, int incrementofeature) {
		int numberOfFeatures = featuresiniciais;
		QualidadeSolucao qualityAttribute = new QualidadeSolucao();
		DadosProblema data;
		XYSeriesCollection dataset = initializeSeries();
		
		GeradorParametros generatorParams = new GeradorParametros(
				numberOfFeatures,
				numeroempregados,
				(int) Math.round(numberOfFeatures * avaliarhabilidadesfeatures),
				taxaprecedencia);
		
		while (numberOfFeatures <= maximofeatures) {
			Map<AlgoritmoEnum, Double[]> qualityValues = new HashMap<>(AlgoritmoEnum.values().length);
			
			for (AlgoritmoEnum algorithm : AlgoritmoEnum.values()) {
				qualityValues.put(algorithm, new Double[reproducaoteste]);
			}
			
			for (int i = 0; i < reproducaoteste; i++) {
				data = GeradorNRP.gerar(generatorParams);
				NextReleaseProblem nrp = new NextReleaseProblem(data.getFeatures(), data.getFuncionarios(), new ParametrosInteracao(numerosemana, horassemana));
				ExecutorAlgoritmo executor = new ExecutorAlgoritmo(nrp, new ParametrosExecutor());
				
				for (AlgoritmoEnum algorithm : AlgoritmoEnum.values()) {
					printTrace(algorithm, numberOfFeatures, i);
					AlgoritmoTempo algoritmoTempo = AlgoritmoTempo.builder().algoritmo(algorithm).inicio(new Date()).build();
					ResultadoExecucao result = executor.executeAlgorithm(algorithm);
					algoritmoTempo.setFim(new Date());
					Double[] values = qualityValues.get(algorithm);
					values[i] = qualityAttribute.getAttribute(result.getSolucao());
					algoritmoTempoList.add(algoritmoTempo);
					Duration duracao = Duration.between(algoritmoTempo.getInicio().toInstant(), algoritmoTempo.getFim().toInstant());
					linha += "Tempo: " + duracao.getNano() + "ns\n";
				}
			}
			
			for (AlgoritmoEnum algorithm : AlgoritmoEnum.values()) {
				dataset.getSeries(algorithm.toString()).add(numberOfFeatures, getAverage(qualityValues.get(algorithm)));
			}
			
			numberOfFeatures += incrementofeature;
			generatorParams.setNumeroDeFeatures(numberOfFeatures);
			generatorParams.setNumeroDeHabilidades((int)Math.round(numberOfFeatures * avaliarhabilidadesfeatures));
		}
		
		return dataset;
	}
	
	/**
	 * Return the generator parameters for a size
	 * @param size the size
	 * @return the generator parameters
	 */
	private GeradorParametros getParameters(int size) {
		int numberOfTasks = (int) Math.round(size/(1.0+ ParametrosPadrao.AVALIAR_FUNCIONARIOS_POR_RECURSO)),
			numberOfEmployees = (int) Math.round(numberOfTasks * ParametrosPadrao.AVALIAR_FUNCIONARIOS_POR_RECURSO),
			numberOfSkills = (int) Math.round(numberOfTasks * ParametrosPadrao.AVALIAR_HABILIDADES_POR_FEATURES);
		
		return new GeradorParametros(numberOfTasks, numberOfEmployees, numberOfSkills, GeradorParametrosPadrao.TAXA_DE_PRECEDENCIA);
	}
	
	/**
	 * Return the average of values
	 * @param values the values
	 * @return the average of the values
	 */
	private double getAverage(Double[] values) {
		double sum = 0.0;
		
		for (Double value : values) {
			sum += value;
		}
		
		return sum/values.length;
	}
	
	/**
	 * Print the current algorithm which is executed
	 * @param algorithm the algorithm
	 * @param size the size of problem
	 * @param repetition the repetition number
	 */
	private void printTrace(AlgoritmoEnum algorithm, int size, int repetition) {
		linha += new StringBuilder("\nExecutando algoritmo ")
				.append(algorithm.toString())
				.append(" (tamanho: ")
				.append(size)
				.append(", i = ")
				.append(repetition)
				.append(") - ");
		System.out.println(linha);
	}
	
	/**
	 * Create a new collection of series for all the algorithms
	 * @return initialized collection of algorithm series
	 */
	private XYSeriesCollection initializeSeries() {
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		for (AlgoritmoEnum algorithm : AlgoritmoEnum.values()) {
			XYSeries series = new XYSeries(algorithm.toString());
			dataset.addSeries(series);
		}
		
		return dataset;
	}
}
