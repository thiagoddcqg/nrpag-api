/**
 * 
 */
package com.nextreleaseproblem.model.operadores;

import com.nextreleaseproblem.model.parametros.PadraoAlgoritmoParametro;
import com.nextreleaseproblem.model.parametros.Funcionario;
import com.nextreleaseproblem.model.parametros.FeaturePlanejada;
import com.nextreleaseproblem.model.NextReleaseProblem;
import com.nextreleaseproblem.model.PlanejamentoSolucao;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.util.ArrayList;
import java.util.List;

public class OperadorMutacaoPlanejamento implements MutationOperator<PlanejamentoSolucao> {

	/**
	 * O número de tarefas do problema
	 */
	private int numeroTarefas;

	/**
	 * Probabilidade de mutação entre 0,0 e 1,0
	 */
	private double probabilidadeMutacao;

	/**
	 * Gerador aleatório
	 */
	private JMetalRandom geradorAleatorio;

	/**
	 * O problema do próximo lançamento que contém a lista de funcionários e tarefas
	 */
	private NextReleaseProblem problema;

	public double getProbabilidadeMutacao() {
		return probabilidadeMutacao;
	}


	/**
	 * 	Constrói um novo OperadorMutacaoPlanejamento com um valor padrão para a probabilidade de mutação
	 *	Valor padrão de {@link PadraoAlgoritmoParametro}
	 * 	@param problema o próximo problema de lançamento a ser resolvido
	 */
	public OperadorMutacaoPlanejamento(NextReleaseProblem problema) {
		this(problema, PadraoAlgoritmoParametro.PROBABILIDADE_MUTACAO(problema.getFeatures().size()));
	}

	/**
	 * Construtor
	 * @param problema O problema
	 * @param probabilidadeMutacao A probabilidade de mutação entre 0,0 e 1,0
	 */
	public OperadorMutacaoPlanejamento(NextReleaseProblem problema, double probabilidadeMutacao) {
		if (probabilidadeMutacao < 0) {
			throw new JMetalException("A probabilidade de mutação é negativa: " + probabilidadeMutacao) ;
		}
		
		this.numeroTarefas = problema.getFeatures().size();
		this.probabilidadeMutacao = probabilidadeMutacao;
		this.problema = problema;
		geradorAleatorio = JMetalRandom.getInstance() ;
	}
	
	
	@Override
	public PlanejamentoSolucao execute(PlanejamentoSolucao pai) {
		PlanejamentoSolucao filho = new PlanejamentoSolucao(pai);
		int numeroTarefasPlanejadas = filho.getQtdFeaturesPlanejadas();
		
		for (int i = 0 ; i < numeroTarefasPlanejadas ; i++) {
			if (realizarMutacao()) { // If we have to do a mutation
				FeaturePlanejada tarefaParaMutar = filho.getPlannedFeature(i);
				if (geradorAleatorio.nextDouble() < 0.5) {
					alterarFuncionario(tarefaParaMutar);
				}
				else {
					alterarTarefa(filho, tarefaParaMutar, i);
				}
			}
		}
		
		for (int i = numeroTarefasPlanejadas; i < problema.getFeatures().size() ; i++) {
			if (realizarMutacao()) {
				adicionarNovaTarefa(filho);
			}
		}
		
		return filho;
	}

	/**
	 * Define se fazemos ou não a mutação
	 * Escolhe aleatoriamente um número e verifica se é menor que a probabilidade de mutação
	 * @return true se a mutação precisar ser feita
	 */
	private boolean realizarMutacao() {
		return geradorAleatorio.nextDouble() <= probabilidadeMutacao;
	}

	/**
	 * Adicione uma tarefa aleatória não planejada ao planejamento
	 * - escolheu aleatoriamente uma tarefa não planejada
	 * - remova-o da lista de tarefas não planejadas da solução
	 * - escolheu aleatoriamente um funcionário
	 * - crie e adicione a tarefa planejada com a tarefa e funcionário escolhido
	 * @param solucao a solução para mutação
	 */
	private void adicionarNovaTarefa(PlanejamentoSolucao solucao) {
		solucao.agendarRecursoAleatorio();
	}

	/**
	 * Substitui uma tarefa por outra.
	 * Pode ser uma tarefa planejada ou não planejada, atualiza a lista de tarefas não planejadas no segundo caso
	 * @param solucao A solução para sofrer mutação
	 * @param tarefaParaAlterar A tarefa planejada a ser modificada
	 * @param posicaoTarefa A posição da tarefa a ser modificada no planejamento (a lista de plannerTask)
	 */
	private void alterarTarefa(PlanejamentoSolucao solucao, FeaturePlanejada tarefaParaAlterar, int posicaoTarefa) {
		int posicaoAleatoria = geradorAleatorio.nextInt(0, numeroTarefas);
		if (posicaoAleatoria < solucao.getQtdFeaturesPlanejadas() - 1) { // If the random selected task is already planned then exchange with the current
			if (posicaoTarefa == posicaoAleatoria) {
				posicaoAleatoria++; // Se houver um problema o tamanho do modulo
			}
			solucao.trocar(posicaoTarefa, posicaoAleatoria);
		}
		else { // Se a tarefa selecionada aleatoriamente ainda não estiver planejada, vamos fazê-lo
			solucao.cancelarAgendamento(tarefaParaAlterar);
			solucao.agendarRecursoAleatorio(posicaoTarefa);
		}
	}

	/**
	 * Alterar o funcionário de uma tarefa planejada por uma aleatória
	 * @param tarefaParaAlterar a tarefa planejada a ser modificada
	 */
	private void alterarFuncionario(FeaturePlanejada tarefaParaAlterar) {
		List<Funcionario> funcionariosQualificados = new ArrayList<>(problema.getSkilledEmployees(tarefaParaAlterar.getFeature().getHabilidadesRequeridas().get(0)));
		funcionariosQualificados.remove(tarefaParaAlterar.getFuncionario());
		if (funcionariosQualificados.size() > 0) {
			tarefaParaAlterar.setFuncionario(funcionariosQualificados.get(geradorAleatorio.nextInt(0, funcionariosQualificados.size()-1)));
		}
	}
}
