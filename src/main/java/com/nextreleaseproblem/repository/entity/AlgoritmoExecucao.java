package com.nextreleaseproblem.repository.entity;

import com.nextreleaseproblem.model.MetaheuristicaEnum;
import com.nextreleaseproblem.model.parametros.AlgoritmoEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Entity
@Data
@Table(name = "ALGORITMO_EXECUCAO")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlgoritmoExecucao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "metaheuristica", nullable = false)
    private AlgoritmoEnum algoritmoEnum;

    @ManyToOne
    @JoinColumn(name = "execucaometaheuristicas_id")
    private ExecucaoMetaheuristicas execucaoMetaheuristicas;

    @Column(name = "minimox")
    private double minimox;

    @Column(name = "maximox")
    private double maximox;

    @Column(name = "minimoy")
    private double minimoy;

    @Column(name = "maximoy")
    private double maximoy;

    @Column(name = "tempo")
    private Duration tempo;

}
