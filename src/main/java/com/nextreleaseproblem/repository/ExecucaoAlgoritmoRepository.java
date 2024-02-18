package com.nextreleaseproblem.repository;

import com.nextreleaseproblem.model.parametros.AlgoritmoEnum;
import com.nextreleaseproblem.repository.entity.ExecucaoMetaheuristicas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExecucaoAlgoritmoRepository extends JpaRepository<ExecucaoMetaheuristicas, Long> {
    List<ExecucaoMetaheuristicas> findByAlgoritmo(AlgoritmoEnum algoritmoEnum);
}