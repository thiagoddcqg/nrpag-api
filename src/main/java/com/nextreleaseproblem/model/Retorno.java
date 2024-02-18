package com.nextreleaseproblem.model;

import com.nextreleaseproblem.repository.entity.AlgoritmoExecucao;
import lombok.Data;

import java.util.List;

@Data
public class Retorno {

    private List<String> resultadoList;
    private List<AlgoritmoExecucao> algoritmoExecucaoList;
    private List<AlgoritmoTempo> algoritmoTempoList;

}
