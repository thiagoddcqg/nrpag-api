package com.nextreleaseproblem.repository;

import com.nextreleaseproblem.repository.entity.AlgoritmoExecucao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AlgoritmoExecucaoRepository extends JpaRepository<AlgoritmoExecucao, Long> {

}