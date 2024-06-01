package com.nextreleaseproblem.service;

import com.nextreleaseproblem.repository.ExecucaoAlgoritmoRepository;
import com.nextreleaseproblem.repository.entity.ExecucaoMetaheuristicas;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExecucaoAlgoritmoService {

    private final ExecucaoAlgoritmoRepository repository;

    public void salvar(ExecucaoMetaheuristicas execucaoMetaheuristicas){
        repository.save(execucaoMetaheuristicas);
    }

    public List<ExecucaoMetaheuristicas> listar(){
        return repository.findAll();
    }

}
