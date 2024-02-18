package com.nextreleaseproblem.repository.entity;

import com.nextreleaseproblem.model.MetaheuristicaEnum;
import com.nextreleaseproblem.util.RuntimeUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "EXECUCAO")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Execucao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "metaheuristica", nullable = false)
    private MetaheuristicaEnum metaheuristica;

    @Column(name = "dataInicio", nullable = false)
    private Date dataInicio;

    // unique = true
    @Column(name = "dataFim", nullable = false)
    private Date dataFim;

    @Column(name = "tamTabu")
    private int tamTabu;

    @Column(name = "iteracoes")
    private int iteracoes;

    @Column(name = "valorSolucao", nullable = false)
    private int valorSolucao;

    // AG
    @Column(name = "taxaMutacao")
    private String taxaMutacao;

    @Column(name = "tamPopulacao")
    private String tamPopulacao;

    @Column(name = "taxaCruzamento")
    private String taxaCruzamento;

    @Column(name = "tipoCruzamento")
    private String tipoCruzamento;

    @Column(name = "tipoSelecao")
    private String tipoSelecao;

    @Column(name = "tamTorneio")
    private String tamTorneio;

    @Column(name = "temperaturaInicial")
    private String temperaturaInicial;

    @Column(name = "taxaResfriamento")
    private String taxaResfriamento;

    @Column(name = "qtdeFeaturesPorPrioridade")
    private String qtdeFeaturesPorPrioridade;

    @Column(name = "tamanhoSolucao")
    private String tamanhoSolucao;

    @Column(name = "provisao")
    private String provisao;

    @Column(name = "idExperimentacao")
    private String idExperimentacao;

    @Column(name = "qtdAcessoFuncaoObjetivo")
    private int qtdAcessoFuncaoObjetivo;

    @Column(name = "alfa")
    private double alfa;

    @Transient
    private double qtdAcessoFuncaoObjetivoMedia;

    @Transient
    private double mediaSolucao;

    @Column(name = "duracaoSprint")
    private int duracaoSprint;

    public String getDuracao() {
        return RuntimeUtil.calcularTempoExecucao(dataInicio, dataFim);
    }

}