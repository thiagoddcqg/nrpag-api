package com.nextreleaseproblem.service;

import com.nextreleaseproblem.repository.entity.Execucao;
import com.nextreleaseproblem.model.MetaheuristicaEnum;
import com.nextreleaseproblem.repository.ExecucaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExecucaoService {

    @Autowired
    private ExecucaoRepository repository;

    public List<Execucao> listAllExecucao() {
        return repository.findAll();
    }

    public void salvarExecucao(Execucao execucao){
        repository.save(execucao);
    }

    public List<Execucao> findByMetaheuristica(MetaheuristicaEnum metaheuristicaEnum){
        List<Execucao> execucaoList = repository.findByMetaheuristica(metaheuristicaEnum);

        execucaoList.forEach(execucao ->
                execucao.setQtdAcessoFuncaoObjetivoMedia(somatorioQtdAcesoFuncaoObjetiva(execucaoList, execucao.getIdExperimentacao())
                        / totalIdExperimentacao(execucaoList, execucao.getIdExperimentacao())));

        execucaoList.forEach(execucao ->
                execucao.setMediaSolucao(somatorioSolucao(execucaoList, execucao.getIdExperimentacao())
                        / totalIdExperimentacao(execucaoList, execucao.getIdExperimentacao())));

        return execucaoList;
    }

    private long totalIdExperimentacao(List<Execucao> execucaoList, String idExperimentacao){
        return execucaoList.stream().filter(
                item -> item.getIdExperimentacao().equals(idExperimentacao)).count();
    }

    private long somatorioQtdAcesoFuncaoObjetiva(List<Execucao> execucaoList, String idExperimentacao){
        return (long) execucaoList.stream()
                .filter(item -> item.getIdExperimentacao().equals(idExperimentacao))
                .mapToDouble(Execucao::getQtdAcessoFuncaoObjetivo)
                .sum();
    }

    private long somatorioSolucao(List<Execucao> execucaoList, String idExperimentacao){
        return (long) execucaoList.stream()
                .filter(item -> item.getIdExperimentacao().equals(idExperimentacao))
                .mapToDouble(Execucao::getValorSolucao)
                .sum();
    }
}
