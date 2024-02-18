package com.nextreleaseproblem.model;

import com.nextreleaseproblem.model.parametros.AlgoritmoEnum;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AlgoritmoTempo {

    private AlgoritmoEnum algoritmo;
    private Date inicio;
    private Date fim;

}
