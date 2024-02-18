package com.nextreleaseproblem.util;

import com.nextreleaseproblem.model.Funcionalidade;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static List<Funcionalidade> criarFeatures(MultipartFile file) throws IOException {
        Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());

        List<Funcionalidade> funcionalidadeList = new ArrayList<Funcionalidade>();
        // Copia os dados do CSV para o objeto
        for (CSVRecord record : csvParser) {
            Funcionalidade funcionalidade = new Funcionalidade();
            funcionalidade.setId(record.get(0));
            funcionalidade.setSistemaModulo(record.get(1));
            funcionalidade.setProjetoModulo(record.get(2));
            funcionalidade.setNumeroProjetoHierarquia(record.get(3));
            funcionalidade.setProjetoHierarquia(record.get(4));
            funcionalidade.setTipo(record.get(5));
            funcionalidade.setSituacao(record.get(6));
            funcionalidade.setTitulo(record.get(7));
            funcionalidade.setAtribuidoPara(record.get(8));
            funcionalidade.setCatalogo(record.get(9));
            funcionalidade.setHet(record.get(10));
            funcionalidade.setQtdServicos(record.get(11));
            funcionalidade.setInicio(record.get(12));
            funcionalidade.setTarefaPai(record.get(13));
            funcionalidade.setQtdAnexos(record.get(14));

            funcionalidadeList.add(funcionalidade);
        }
        csvParser.close();
        reader.close();

        return funcionalidadeList;
    }
}
