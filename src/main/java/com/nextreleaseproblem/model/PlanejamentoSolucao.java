/**
 * A solution of the NRP
 * It contains a plannedFeatures list which give the order of the features which are planned
 */
package com.nextreleaseproblem.model;

import com.nextreleaseproblem.model.parametros.*;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.impl.AbstractGenericSolution;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlanejamentoSolucao extends AbstractGenericSolution<FeaturePlanejada, NextReleaseProblem> {

	/**
	 * Id gerado
	 */
	private static final long serialVersionUID = 615615442782301271L;
	
	/**
	 * Recursos planejados para a solução
	 */
	private List<FeaturePlanejada> featurePlanejadas;
	
	/**
	 * Recursos não planejados para a solução
	 */
	private List<Feature> featuresNaoPlanejadas;

	/**
	 * A hora final da solução
	 * É atualizado somente quando o campo estaAtualizado é verdadeiro
	 */
	private double dataFim;

	/**
	 * O planejamento da semana dos funcionários
	 */
	private Map<Funcionario, List<DisponibilidadeSemanalFuncionario>> planejamentoFuncionarios;


	public double getDataFim() {
		return dataFim;
	}
	

	public void setDataFim(double dataFim) {
		this.dataFim = dataFim;
	}
	
	public int getQtdFeaturesPlanejadas() {
		return featurePlanejadas.size();
	}

	/**
	 * @return recursoPlanejados
	 */
	public List<FeaturePlanejada> getFeaturesPlanejadas() {
		return new ArrayList<>(featurePlanejadas);
	}
	
	/**
	 * Return a feature planejada do <code>posicao</code> na lista
	 * @param posicao a posição na lista
	 * @return a feature planejada ou null
	 */
	public FeaturePlanejada getPlannedFeature(int posicao) {
		if (posicao >= 0 && posicao < featurePlanejadas.size())
			return featurePlanejadas.get(posicao);
		return null;
	}
	
	/**
	 * Pega uma cópia da feature planejada na posição original na lista
	 * @param posicaoInicial the begin position
	 * @return a copy of the end list
	 */
	public List<FeaturePlanejada> getCopiaSublistaRecursosPlanejadosFinais(int posicaoInicial) {
		return new ArrayList<>(featurePlanejadas.subList(posicaoInicial, featurePlanejadas.size()));
	}

	/**
	 * @return a lista de features não planejadas
	 */
	private List<Feature> getFeaturesNaoPlanejadas() {
		return featuresNaoPlanejadas;
	}
	
	/**
	 * @return planejamentoFuncionários
	 */
	public Map<Funcionario, List<DisponibilidadeSemanalFuncionario>> getPlanejamentoFuncionarios() {
		return planejamentoFuncionarios;
	}

	/**
	 * @param planejamentoFuncionarios
	 */
	public void setPlanejamentoFuncionarios(Map<Funcionario, List<DisponibilidadeSemanalFuncionario>> planejamentoFuncionarios) {
		this.planejamentoFuncionarios = planejamentoFuncionarios;
	}

	/**
	 * Construtor
	 * Inicializa um conjunto aleatório de recursos planejados
	 * @param problema
	 */
	public PlanejamentoSolucao(NextReleaseProblem problema) {
		super(problema);
	    numberOfViolatedConstraints = 0;

		inicializarVariaveisFeaturesPlanejadas();
	    initializeObjectiveValues();
	}

	/**
	 * Construtor
	 * inicializar um conjunto aleatório de recursos planejados
	 * @param problema
	 */
	public PlanejamentoSolucao(NextReleaseProblem problema, List<FeaturePlanejada> recursosPlanejados) {
		super(problema);
	    numberOfViolatedConstraints = 0;

	    featuresNaoPlanejadas = new CopyOnWriteArrayList<Feature>();
		featuresNaoPlanejadas.addAll(problema.getFeatures());
		this.featurePlanejadas = new CopyOnWriteArrayList<FeaturePlanejada>();
		for (FeaturePlanejada featurePlanejada : recursosPlanejados) {
			agendarFinal(featurePlanejada.getFeature(), featurePlanejada.getFuncionario());
		}
	    initializeObjectiveValues();
	}

	/**
	 * construtor de cópia
	 * @param origem PlanningSoltion a ser copiado
	 */
	public PlanejamentoSolucao(PlanejamentoSolucao origem) {
		super(origem.problem);

	    numberOfViolatedConstraints = origem.numberOfViolatedConstraints;
	    
	    featurePlanejadas = new CopyOnWriteArrayList<>();
	    for (FeaturePlanejada featurePlanejada : origem.getFeaturesPlanejadas()) {
			featurePlanejadas.add(new FeaturePlanejada(featurePlanejada));
		}
	    
	    // Copy constraints and quality
	    this.attributes.putAll(origem.attributes);
	    
	    planejamentoFuncionarios = new HashMap<>();
	    
	    for (Funcionario e : origem.planejamentoFuncionarios.keySet()) {
	    	List<DisponibilidadeSemanalFuncionario> old = origem.planejamentoFuncionarios.get(e);
			List<DisponibilidadeSemanalFuncionario> availabilities = new ArrayList<>(old.size());
			for (DisponibilidadeSemanalFuncionario disponibilidadeSemanalFuncionario : old) {
				availabilities.add(new DisponibilidadeSemanalFuncionario(disponibilidadeSemanalFuncionario));
			}
			planejamentoFuncionarios.put(e, availabilities);
		}
	    
	    for (int i = 0 ; i < origem.getNumberOfObjectives() ; i++) {
	    	this.setObjective(i, origem.getObjective(i));
	    }
	    
	    dataFim = origem.getDataFim();
	    featuresNaoPlanejadas = new CopyOnWriteArrayList<>(origem.getFeaturesNaoPlanejadas());
	}


	/**
	 * Troque os dois recursos nas posições pos1 e pos2
	 * @param pos1 A posição do primeiro recurso planejado para troca
	 * @param pos2 A posição do segundo recurso planejado para troca
	 */
	public void trocar(int pos1, int pos2) {
		if (pos1 >= 0 && pos2 >= 0 && pos1 < featurePlanejadas.size() && pos2 < featurePlanejadas.size() && pos1 != pos2) {
			FeaturePlanejada feature1 = featurePlanejadas.get(pos1);
			featurePlanejadas.set(pos1, new FeaturePlanejada(featurePlanejadas.get(pos2)));
			featurePlanejadas.set(pos2, new FeaturePlanejada(feature1));
		}
	}

	/**
	 * Calcule a soma da prioridade de cada recurso
	 * @return a pontuação de prioridade
	 */
	public double getPrioridadePontuacao() {
		double pontuacao = problem.getPontuacaoTrabalhada();
		
		for (FeaturePlanejada featurePlanejada : featurePlanejadas) {
			pontuacao -= featurePlanejada.getFeature().getPrioridade().getPontuacao();
		}
		
		return pontuacao;
	}

	/**
	 * Retorna todos as features planejados feitos por um funcionário específico
	 * @param funcionario O funcionário
	 * @return Uma lista features feita pelo funcionário
	 */
	public List<FeaturePlanejada> getFeaturesFeitasPor(Funcionario funcionario) {
		List<FeaturePlanejada> featuresFuncionario = new ArrayList<>();

		for (FeaturePlanejada featurePlanejada : featurePlanejadas) {
			if (featurePlanejada.getFuncionario() == funcionario) {
				featuresFuncionario.add(featurePlanejada);
			}
		}

		return featuresFuncionario;
	}

	/**
	 * Retorne verdadeiro se a feature já estiver nas features planejadas
	 * @param feature feature a ser pesquisada
	 * @return true se uma feature já estiver planejada
	 */
	public boolean isPlanejada(Feature feature) {
		boolean encontrada = false;
		Iterator<FeaturePlanejada> it = featurePlanejadas.iterator();
		
		while (!encontrada && it.hasNext()) {
			FeaturePlanejada featurePlanejada = (FeaturePlanejada) it.next();
			if (featurePlanejada.getFeature().equals(feature)) {
				encontrada = true;
			}
		}
		
		return encontrada;
	}


	/**
	 * Retorna uma feature planejado correspondente a uma feature fornecido no parâmetro
	 * @param feature Uma feature de pesquisado
	 * @return Uma feature configurado ou nulo se ainda não estiver planejado
	 */
	public FeaturePlanejada encontreFeaturePlanejada(Feature feature) {
		for (Iterator<FeaturePlanejada> iterator = featurePlanejadas.iterator(); iterator.hasNext();) {
			FeaturePlanejada featurePlanejada = iterator.next();
			if (featurePlanejada.getFeature().equals(feature)) {
				return featurePlanejada;
			}
		}
		
		return null;
	}

	/**
	 * Inicialize as features desejadas aleatoriamente
	 * @param numero o número de features a serem planejados
	 */
	private void inicializarFeaturesPlanejadosAleatoriamente(int numero) {
		Feature feature;
		List<Funcionario> funcionariosQualificados;
		
		for (int i = 0 ; i < numero ; i++) {
			feature = featuresNaoPlanejadas.get(randomGenerator.nextInt(0, featuresNaoPlanejadas.size()-1));
			funcionariosQualificados = problem.getSkilledEmployees(feature.getHabilidadesRequeridas().get(0));
			agendarFinal(feature,
					funcionariosQualificados.get(randomGenerator.nextInt(0, funcionariosQualificados.size()-1)));
		}
	}

	/**
	 * Inicialize as variáveis
	 * Carregue um número aleatório de features planejadas
	 */
	private void inicializarVariaveisFeaturesPlanejadas() {
		int qtdeFeatures = problem.getFeatures().size();
		int numeroFeatureParaCriar = randomGenerator.nextInt(0, qtdeFeatures);
		
		featuresNaoPlanejadas = new CopyOnWriteArrayList<Feature>();
		featuresNaoPlanejadas.addAll(problem.getFeatures());
		featurePlanejadas = new CopyOnWriteArrayList<FeaturePlanejada>();
	
		if (randomGenerator.nextDouble() > PadraoAlgoritmoParametro.TAXA_GERACAO_NAO_GERADA_ALEATORIAMENTE) {
			inicializarFeaturesPlanejadosAleatoriamente(numeroFeatureParaCriar);
		}
		else {
			inicializarFeaturesPlanejadasComPrecedentes(numeroFeatureParaCriar);
		}
	}

	/**
	 * Inicializa as features planejadas considerando as precedências
	 * @param numero o número de recursos a serem planejados
	 */
	private void inicializarFeaturesPlanejadasComPrecedentes(int numero) {
		Feature feature;
		List<Funcionario> funcionariosQualificados;
		List<Feature> possivelFeature = atualizarFeaturesPossíveis();
		int i = 0;
		while (i < numero && possivelFeature.size() > 0) {
			feature = possivelFeature.get(randomGenerator.nextInt(0, possivelFeature.size()-1));
			funcionariosQualificados = problem.getSkilledEmployees(feature.getHabilidadesRequeridas().get(0));
			agendarFinal(feature,
					funcionariosQualificados.get(randomGenerator.nextInt(0, funcionariosQualificados.size()-1)));
			possivelFeature = atualizarFeaturesPossíveis();
			i++;
		}
	}

	/**
	 * Redefine o horário de início de todos as features planejados para 0,0
	 */
	public void reiniciarHoras() {
		for (FeaturePlanejada featurePlanejada : featurePlanejadas) {
			featurePlanejada.setHoraInicio(0.0);
			featurePlanejada.setHoraFim(0.0);
		}
	}

	/**
	 * Agende uma feature planejado para uma posição no planejamento
	 * @param position a posição do planejamento
	 * @param feature a feature para planejar
	 * @param funcionario o funcionário que irá executar a feature
	 */
	public void agendar(int position, Feature feature, Funcionario funcionario) {
		featuresNaoPlanejadas.remove(feature);
		featurePlanejadas.add(position, new FeaturePlanejada(feature, funcionario));
	}

	/**
	 * Agende uma feature no planejamento
	 * Remova uma feature das features desfeitos
	 * e adicione a feature planejado no final da lista de recursos planejados
	 * @param feature a feature de agendamento
	 * @param funcionario o funcionário que irá realizar o recurso
	 */
	public void agendarFinal(Feature feature, Funcionario funcionario) {
		if (!isPlanejada(feature)) {
			featuresNaoPlanejadas.remove(feature);
			featurePlanejadas.add(new FeaturePlanejada(feature, funcionario));
		}
	}

	/**
	 * Agende uma feature aleatório desfeito para uma posição aleatória no planejamento
	 */
	public void agendarRecursoAleatorio() {
		agendarRecursoAleatorio(randomGenerator.nextInt(0, featurePlanejadas.size()));
	}

	/**
	 * Agenda um recurso aleatório para a posição de inserção da lista de planejamento
	 * @param posicaoInsercao a posição de inserção
	 */
	public void agendarRecursoAleatorio(int posicaoInsercao) {
		if (featuresNaoPlanejadas.size() <= 0)
			return;
		Feature novaFeature = featuresNaoPlanejadas.get(randomGenerator.nextInt(0, featuresNaoPlanejadas.size() -1));
		List<Funcionario> funcionariosQualificados = problem.getSkilledEmployees(novaFeature.getHabilidadesRequeridas().get(0));
		Funcionario novoFuncionario = funcionariosQualificados.get(randomGenerator.nextInt(0, funcionariosQualificados.size()-1));
		agendar(posicaoInsercao, novaFeature, novoFuncionario);
	}

	/**
	 * Agende o recurso planejado em uma posição aleatória no planejamento
	 * @param recursoPlanejado a funcionalidade planejada para integrar ao planejamento
	 */
	public void agendarAleatoriamente(FeaturePlanejada recursoPlanejado) {
		agendar(randomGenerator.nextInt(0, featurePlanejadas.size()), recursoPlanejado.getFeature(), recursoPlanejado.getFuncionario());
	}

	/**
	 * Cancelar agendamento de uma feature: remova-o das features planejados e adicione-o aos desfeitos
	 * @param featurePlanejada o recurso planejado para desprogramar
	 */
	public void cancelarAgendamento(FeaturePlanejada featurePlanejada) {
		if (isPlanejada(featurePlanejada.getFeature())) {
			featuresNaoPlanejadas.add(featurePlanejada.getFeature());
			featurePlanejadas.remove(featurePlanejada);
				
		}
	}

	/**
	 * Cria uma lista dos possíveis features a serem executados em relação às precedências das features desfeitos
	 * @return a lista de features possíveis de fazer
	 */
	private List<Feature> atualizarFeaturesPossíveis() {
		List<Feature> possivelFeature = new ArrayList<>();
		boolean possivel;
		int i;
		
		for (Feature feature : featuresNaoPlanejadas) {
			possivel = true;
			i = 0;
			while (possivel && i < feature.getFeatuesAnteriores().size()) {
				if (!isPlanejada(feature.getFeatuesAnteriores().get(i))) {
					possivel = false;
				}
				i++;
			}
			if (possivel) {
				possivelFeature.add(feature);
			}
		}
		
		return possivelFeature;
	}

	@Override
	public String getVariableValueString(int index) {
		return getVariableValueString(index).toString();
	}

	@Override
	public Solution<FeaturePlanejada> copy() {
		return new PlanejamentoSolucao(this);
	}
	
	@Override
	public int hashCode() {
		return getFeaturesPlanejadas().size();
	};
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (getClass() != obj.getClass())
			return false;

		PlanejamentoSolucao other = (PlanejamentoSolucao) obj;
		
		int size = this.getFeaturesPlanejadas().size();
		boolean equals = other.getFeaturesPlanejadas().size() == size;
		int i = 0;
		while (equals && i < size) {
			if (!other.getFeaturesPlanejadas().contains(this.getFeaturesPlanejadas().get(i))) {
				equals = false;
			}
			i++;
		}

		return equals;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String lineSeparator = System.getProperty("line.separator");
		
		sb.append('(');
		for (int i = 0 ; i < getNumberOfObjectives() ; i++) {
			sb.append(getObjective(i)).append('\t');
		}
		
		sb.append(new NumberOfViolatedConstraints<>().getAttribute(this));
		sb.append(')').append(lineSeparator);
		
		for (FeaturePlanejada feature : getFeaturesPlanejadas()) {
			sb.append("-").append(feature);
			sb.append(lineSeparator);
		}
		
		sb.append("Data fim: ").append(getDataFim()).append(System.getProperty("line.separator"));
		
		return sb.toString();
	}
}
