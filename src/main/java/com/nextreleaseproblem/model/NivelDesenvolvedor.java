package com.nextreleaseproblem.model;

import lombok.Data;
import lombok.Getter;

@Getter
public enum NivelDesenvolvedor {

    JUNIOR(1), PLENO(2), SENIOR(3);

    private int fator;

    NivelDesenvolvedor(int fator) {
        this.fator = fator;
    }
}
