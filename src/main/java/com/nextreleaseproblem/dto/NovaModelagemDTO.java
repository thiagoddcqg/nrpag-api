package com.nextreleaseproblem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NovaModelagemDTO {
    private int id;
    private double valorNegocio;
    private double esforco;
}
