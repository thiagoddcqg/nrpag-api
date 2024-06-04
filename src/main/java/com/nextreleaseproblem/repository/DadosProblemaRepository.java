package com.nextreleaseproblem.repository;

import com.nextreleaseproblem.model.parametros.DadosProblema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DadosProblemaRepository extends JpaRepository<DadosProblema, Long> {

}
