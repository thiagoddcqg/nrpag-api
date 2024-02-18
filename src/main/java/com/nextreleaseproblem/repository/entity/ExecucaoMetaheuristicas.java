package com.nextreleaseproblem.repository.entity;

import com.nextreleaseproblem.model.parametros.AlgoritmoEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "EXECUCAO_METAHEURISTICAS")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExecucaoMetaheuristicas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //#####Entrada
    @Enumerated(EnumType.STRING)
    @Column(name = "algoritmo")
    private AlgoritmoEnum algoritmo;

    @Column(name = "numsemanas")
    private int numsemanas;

    @Column(name = "hrsporsemana")
    private double hrsporsemana;

    @Column(name = "numfeatures")
    private int numfeatures;

    @Column(name = "numempregados")
    private int numempregados;

    @Column(name = "numerohabilidades")
    private int numerohabilidades;

    @Column(name = "taxaprecedencia")
    private double taxaprecedencia;
    //#####Fim Entrada

    @Column(name = "htmlString", length = 2000000)
    private String htmlstring;

    @Column(name = "imagemGraficoBase64", length = 1000000)
    private String imagemGraficoBase64;

    @Column(name = "datainicio")
    private Date dataInicio;

    @Column(name = "datafim")
    private Date dataFim;

    @Column(name = "retornoexecucao", length = 2000000)
    private String retornoExecucao;

    @Column(name = "maximofuncionarios")
    private int maximoFuncionarios;

    @Column(name = "qtdfuncionariosiniciais")
    private int qtdFuncionariosIniciais;

    @Column(name = "avaliarhabilidadesfeatures")
    private double avaliarHabilidadesFeatures;

    @Column(name = "reproducaoteste")
    private int reproducaoTeste;

    @Column(name = "incrementofuncionarios")
    private int incrementoFuncionarios;

    @Column(name = "featuresiniciais")
    private int featuresIniciais;

    @Column(name = "maximofeatures")
    private int maximoFeatures;

    @Column(name = "incrementofeature")
    private int incrementoFeature;

    @Column(name = "tamanhomaximoproblema")
    private int tamanhoMaximoProblema;

    @Column(name = "tamanhoinicial")
    private int tamanhoInicial;

    @Column(name = "incrementotamanho")
    private int incrementoTamanho;

    @OneToMany(mappedBy = "execucaoMetaheuristicas", cascade = CascadeType.ALL)
    private List<AlgoritmoExecucao> algoritmoExecucaoList;

}
