package com.nextreleaseproblem.repository;

import com.nextreleaseproblem.repository.entity.Execucao;
import com.nextreleaseproblem.model.MetaheuristicaEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExecucaoRepository extends JpaRepository<Execucao, Long> {
    List<Execucao> findByMetaheuristica(MetaheuristicaEnum metaheuristicaEnum);
}
