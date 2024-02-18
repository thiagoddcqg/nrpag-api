/**
 * 
 */
package com.nextreleaseproblem.model;

import com.nextreleaseproblem.model.parametros.*;
import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.impl.AbstractGenericProblem;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Objetivos:
 * 0: Fazendo a pontuação mais alta com prioridade
 * 1: A data final mais curta
 */
public class NextReleaseProblem extends AbstractGenericProblem<PlanejamentoSolucao> implements ConstrainedProblem<PlanejamentoSolucao> {


	/**
	 * Id
	 */
	private static final long serialVersionUID = 3302475694747789178L;
	
	/**
	 * Features disponíveis para a iteração
	 */
	private List<Feature> features;

	/**
	 * Funcionários disponíveis para a iteração
	 */
	private List<Funcionario> funcionarios;

	/**
	 * Número de restrições violadas
	 */
	private NumberOfViolatedConstraints<PlanejamentoSolucao> numeroRestricoesVioladas;

	/**
	 * Número de restrições violadas
	 */
	private OverallConstraintViolation<PlanejamentoSolucao> violacaoRestricaoGeral;

	/**
	 * O atributo de qualidade da solução
	 */
	private QualidadeSolucao qualidadeSolucao;

	/**
	 * Funcionários classificados por habilidade
	 * Um funcionário está na lista de todas as suas habilidades
	 */
	private Map<Habilidade, List<Funcionario>> funcionariosQualificados;

	/**
	 * O número de semanas da iteração
	 */
	private int numeroSemanas;

	/**
	 * O número de horas trabalhadas por semana
	 */
	private double numeroHorasSemana;

	/**
	 * O total de uma restrição violada
	 */
	private double restricaoPrecedenciaGeral;

	/**
	 * A pontuação de prioridade se não houver nenhum recurso planejado
	 */
	private double pontuacaoTrabalhada;

	/**
	 * A pior data de término, se não houver nenhum recurso planejado
	 */
	private double piorDataTermino;

	/**
	 * O índice do objetivo de pontuação prioritária na lista de objetivos
	 */
	public final static int INDICE_PRIORIDADE_OBJETIVO = 0;

	/**
	 * O índice do objetivo da data final na lista de objetivos
	 */
	public final static int INDICE_DATA_FIM_OBJETIVO = 1;
	
	

	public List<Feature> getFeatures() {
		return features;
	}


	public void setFeatures(List<Feature> features) {
		this.features = features;
	}
	

	public int getNumeroSemanas() {
		return numeroSemanas;
	}
	

	public double getNbHoursByWeek() {
		return numeroHorasSemana;
	}
	

	public int getNumberOfEmployees() {
		return funcionarios.size();
	}
	

	public NumberOfViolatedConstraints<PlanejamentoSolucao> getNumeroRestricoesVioladas() {
		return numeroRestricoesVioladas;
	}


	public List<Funcionario> getSkilledEmployees(Habilidade habilidade) {
		return funcionariosQualificados.get(habilidade);
	}
	

	public List<Funcionario> getEmployees() {
		return funcionarios;
	}
	

	public double getPontuacaoTrabalhada() {
		return pontuacaoTrabalhada;
	}


	/**
	 * Construtor
	 * @param features recursos da iteração Funcionários
	 * @param funcionarios disponíveis durante a iteração
	 * @param parametrosInteracao Os parâmetros da iteração
	 */
	public NextReleaseProblem(List<Feature> features, List<Funcionario> funcionarios, ParametrosInteracao parametrosInteracao) {
		this.funcionarios = funcionarios;
		this.numeroSemanas = parametrosInteracao.getNumeroSemana();
		this.numeroHorasSemana = parametrosInteracao.getHorasSemana();
		
		funcionariosQualificados = new HashMap<>();
		for (Funcionario funcionario : funcionarios) {
			for (Habilidade habilidade : funcionario.getAbilidades()) {
				List<Funcionario> fucionarioList = funcionariosQualificados.get(habilidade);
				if (fucionarioList == null) {
					fucionarioList = new ArrayList<>();
					funcionariosQualificados.put(habilidade, fucionarioList);
				}
				fucionarioList.add(funcionario);
			}
		}
		
		this.features = new ArrayList<>();
		for (Feature feature : features) {
			if (funcionariosQualificados.get(feature.getHabilidadesRequeridas().get(0)) != null) {
				if (features.containsAll(feature.getFeatuesAnteriores())) {
					this.features.add(feature);
				}
			}
		}
		
		piorDataTermino = numeroSemanas * numeroHorasSemana;
		setNumberOfVariables(1);
		setName("Problema da próxima versão");
		setNumberOfObjectives(2);
		inicializarPiorPontuacao();
		inicializarNumeroRestricoes();
		
		numeroRestricoesVioladas = new NumberOfViolatedConstraints<PlanejamentoSolucao>();
		violacaoRestricaoGeral = new OverallConstraintViolation<>();
		qualidadeSolucao = new QualidadeSolucao();
	}


	/**
	 * Inicializa a pior pontuação
	 * Correspondente à adição de cada pontuação de prioridade de recurso
	 */
	private void inicializarPiorPontuacao() {
		pontuacaoTrabalhada = 0.0;
		for (Feature feature : features) {
			pontuacaoTrabalhada += feature.getPrioridade().getPontuacao();
		}
	}

	/**
	 * Inicializa o número de restrições do problema
	 * Correspondente ao número de precedências mais uma para o overflow de tempo
	 */
	private void inicializarNumeroRestricoes() {
		int numeroRestricoes = 0;
		
		//Precedences
		for (Feature feature : features) {
			numeroRestricoes += feature.getFeatuesAnteriores().size();
		}
		
		restricaoPrecedenciaGeral = 1.0 / numeroRestricoes;
		
		numeroRestricoes++;
		
		setNumberOfConstraints(numeroRestricoes);
	}


	@Override
	public PlanejamentoSolucao createSolution() {
		return new PlanejamentoSolucao(this);
	}

	@Override
	public void evaluate(PlanejamentoSolucao solucao) {
		double novaHoraInicio;
		double horaFimPlanejamento = 0.0;
		Map<Funcionario, List<DisponibilidadeSemanalFuncionario>> horariosFuncionarios = new HashMap<>();
		List<FeaturePlanejada> featurePlanejadas = solucao.getFeaturesPlanejadas();
			
		solucao.reiniciarHoras();
		
		for (FeaturePlanejada featurePlanejada : featurePlanejadas) {
			novaHoraInicio = 0.0;
			Feature feature = featurePlanejada.getFeature();

			// Verifica o horário de término das features anteriores
			for (Feature featureAnterior : feature.getFeatuesAnteriores()) {
				FeaturePlanejada featureAnteriorPlanejada = solucao.encontreFeaturePlanejada(featureAnterior);
				if (featureAnteriorPlanejada != null) {
					novaHoraInicio = Math.max(novaHoraInicio, featureAnteriorPlanejada.getHoraFim());
				}
			}

			// Verifica a disponibilidade do funcionário
			Funcionario funcionario = featurePlanejada.getFuncionario();
			List<DisponibilidadeSemanalFuncionario> horariosDosFuncionarios = horariosFuncionarios.get(funcionario);
			int semana;
			
			if (horariosDosFuncionarios == null) {
				horariosDosFuncionarios = new ArrayList<>();
				horariosDosFuncionarios.add(new DisponibilidadeSemanalFuncionario(novaHoraInicio, funcionario.getDisponibilidadeSemanal()));
				horariosFuncionarios.put(funcionario, horariosDosFuncionarios);
				semana = 0;
			}
			else {
				semana = horariosDosFuncionarios.size()-1;
				novaHoraInicio = Math.max(novaHoraInicio, horariosDosFuncionarios.get(semana).getHoraFim());
			}

			featurePlanejada.setHoraInicio(novaHoraInicio);
			
			double permanecerHorarioFeature = featurePlanejada.getFeature().getDuracao();
			double horasRestantesSemana;
			DisponibilidadeSemanalFuncionario disponibilidadeSemanaAtual;
			
			semana = ((int) novaHoraInicio) / (int)numeroHorasSemana;
			
			while (novaHoraInicio > (semana + 1) * numeroHorasSemana) {
				semana++;
			}

			do {
				disponibilidadeSemanaAtual = horariosDosFuncionarios.get(horariosDosFuncionarios.size() - 1);
				double newBeginHourInWeek = Math.max(novaHoraInicio, disponibilidadeSemanaAtual.getHoraFim());
				horasRestantesSemana = Math.min((semana + 1) * numeroHorasSemana - newBeginHourInWeek //Horas restantes na semana
						, disponibilidadeSemanaAtual.getHorasRestantesDisponiveis());
				
				if (permanecerHorarioFeature <= horasRestantesSemana) { // O recurso pode ser encerrado antes do final da semana
					disponibilidadeSemanaAtual.setHorasRestantesDisponiveis(disponibilidadeSemanaAtual.getHorasRestantesDisponiveis() - permanecerHorarioFeature);
					disponibilidadeSemanaAtual.setHoraFim(newBeginHourInWeek + permanecerHorarioFeature);
					permanecerHorarioFeature = 0.0;
				}
				else {
					disponibilidadeSemanaAtual.setHorasRestantesDisponiveis(disponibilidadeSemanaAtual.getHorasRestantesDisponiveis() - horasRestantesSemana);
					disponibilidadeSemanaAtual.setHoraFim(disponibilidadeSemanaAtual.getHoraFim() + horasRestantesSemana);
					permanecerHorarioFeature -= horasRestantesSemana;
					semana++;
					horariosDosFuncionarios.add(new DisponibilidadeSemanalFuncionario(semana*numeroHorasSemana, funcionario.getDisponibilidadeSemanal()));
				}
				disponibilidadeSemanaAtual.addFeaturePlanejada(featurePlanejada);
			} while (permanecerHorarioFeature > 0.0);
			
			featurePlanejada.setHoraFim(disponibilidadeSemanaAtual.getHoraFim());

			horaFimPlanejamento = Math.max(featurePlanejada.getHoraFim(), horaFimPlanejamento);
		}
		
		solucao.setPlanejamentoFuncionarios(horariosFuncionarios);
		solucao.setDataFim(horaFimPlanejamento);
		solucao.setObjective(INDICE_PRIORIDADE_OBJETIVO, solucao.getPrioridadePontuacao());
		solucao.setObjective(INDICE_DATA_FIM_OBJETIVO,
				featurePlanejadas.size() == 0 ? piorDataTermino : horaFimPlanejamento);
		qualidadeComputacional(solucao);
	}

	@Override
	public void evaluateConstraints(PlanejamentoSolucao solution) {
		int precedenciasVioladas = 0;
		int restricoesVioladas;
		double geral;
		
		for (FeaturePlanejada feature : solution.getFeaturesPlanejadas()) {
			for (Feature featureAnterior : feature.getFeature().getFeatuesAnteriores()) {
				FeaturePlanejada featureAnteriorPlanejada = solution.encontreFeaturePlanejada(featureAnterior);
				if (featureAnteriorPlanejada == null || featureAnteriorPlanejada.getHoraFim() > feature.getHoraInicio()) {
					precedenciasVioladas++;
				}
			}
		}
		
		geral = -1.0 * precedenciasVioladas * restricaoPrecedenciaGeral;
		restricoesVioladas = precedenciasVioladas;
		
		if (solution.getDataFim() > numeroSemanas * numeroHorasSemana) {
			restricoesVioladas++;
			geral -= 1.0;
		}
		
		numeroRestricoesVioladas.setAttribute(solution, restricoesVioladas);
		violacaoRestricaoGeral.setAttribute(solution, geral);
		if (restricoesVioladas > 0) {
			qualidadeSolucao.setAttribute(solution, 0.0);
		}
	}

	/**
	 *Atualiza o atributo qualidade da solução
	 * @param solucao A solução para avaliar qualidade
	 */
	private void qualidadeComputacional(PlanejamentoSolucao solucao) {
		double qualidadeGlobal;
		double dataFimQualidade = 1.0 - (solucao.getObjective(INDICE_DATA_FIM_OBJETIVO) / piorDataTermino);
		double prioridadeQualidade = 1.0 - (solucao.getObjective(INDICE_PRIORIDADE_OBJETIVO) / pontuacaoTrabalhada);
		
		qualidadeGlobal = (dataFimQualidade + prioridadeQualidade) / 2;
		qualidadeSolucao.setAttribute(solucao, qualidadeGlobal);
	}
}
