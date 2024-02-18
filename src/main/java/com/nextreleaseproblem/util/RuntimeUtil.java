package com.nextreleaseproblem.util;

import com.nextreleaseproblem.model.MetaheuristicaEnum;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RuntimeUtil {
    public static String calcularTempoExecucao(Date inicio, Date fim){
        System.out.println("Fim: " + fim);
        // Criar duas instâncias de java.util.Calendar
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        // Definir as datas nos objetos Calendar
        cal1.setTime(inicio);
        cal2.setTime(fim);

        // Obter o valor do tempo em segundos para cada data
        long timeInSeconds1 = cal1.getTimeInMillis() / 1000;
        long timeInSeconds2 = cal2.getTimeInMillis() / 1000;

        // Calcular a diferença em segundos
        long differenceInSeconds = Math.abs(timeInSeconds1 - timeInSeconds2);
        return differenceInSeconds + " segundos";
    }

    public static String generateIdExperimentacao(MetaheuristicaEnum metaheuristicaEnum){
        return metaheuristicaEnum.getSigla() + obterDataAtualFormatada();
    }

    public static String obterDataAtualFormatada() {
        // Obtém a data atual
        Date dataAtual = new Date();

        // Define o formato desejado
        SimpleDateFormat formato = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");

        // Formata a data e retorna como uma string
        return formato.format(dataAtual);
    }

}
