package com.nextreleaseproblem.util;

import com.nextreleaseproblem.model.Funcionalidade;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GraphUtil {

    //Dataset do grafico burndown
    private static DefaultCategoryDataset createDatasetBurndown(String conteudo, List<Funcionalidade> funcionalidadeList) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        int total = funcionalidadeList.size();

        String[] itens = conteudo.split("\n");
        String sprint = "";
        int cont = 0;
        int valorTotal = 0;
        dataset.addValue(total, "Features Restantes", "");
        for(String item : itens){
            if(item.contains("Total features utilizadas")){
                cont++;
                String valor = item.split(":")[1];
                int valorInt = Integer.parseInt(valor.trim());
                valorTotal = valorTotal + valorInt;

                sprint = "Sprint " + cont;
                int resultado = total - valorTotal;

                dataset.addValue(resultado, "Features Restantes", sprint);
            }
        }

        return dataset;
    }

    //Método que gera o gráfico burndown
    public static String gerarGraficoBurndown(String dados, List<Funcionalidade> funcionalidadeList){
        DefaultCategoryDataset dataset = createDatasetBurndown(dados, funcionalidadeList);

        JFreeChart chart = ChartFactory.createLineChart(
                "Gráfico de Burndown", // Título do gráfico
                "Sprints", // Rótulo do eixo X
                "Features Restantes", // Rótulo do eixo Y
                dataset, // Conjunto de dados
                PlotOrientation.VERTICAL, // Orientação do gráfico
                true, // Exibir legenda
                true, // Usar tooltips
                false // URLs?
        );

        String burndown = generateChartBase64(chart, 800, 600);
        return  burndown;
    }

    //Método que gera o gráfico de gantt
    public static String gerarGraficoGantt(String dados){
        // Crie o conjunto de tarefas para o cronograma
        IntervalCategoryDataset dataset = createDataset(dados);

        // Crie o gráfico
        JFreeChart chart = ChartFactory.createGanttChart(
                "Gráfico de Gantt", // Título do gráfico
                "Features", // Rótulo do eixo X
                "Sprints", // Rótulo do eixo Y
                dataset, // Conjunto de dados
                true, // Exibir legenda
                true, // Usar tooltips
                false // URLs?
        );

        // Customizações adicionais
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        GanttRenderer renderer = new GanttRenderer();
        renderer.setSeriesPaint(86522, Color.BLUE);
        renderer.setSeriesPaint(86281, Color.GREEN);

        plot.setRenderer(renderer);
        DateAxis axis = (DateAxis) plot.getRangeAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("dd/MM/yyyy"));
        axis.setTickUnit(new DateTickUnit(DateTickUnitType.DAY, 1));

        // Converta o gráfico em uma string base64 e adicione-a ao modelo
        String chartBase64 = generateChartBase64(chart, 800, 12000);
        return chartBase64;
    }

    //Cria do dataset do grafico de gantt
    private static IntervalCategoryDataset createDataset(String conteudo) {
        TaskSeries taskSeries = new TaskSeries("Features");

        String[] itens = conteudo.split("\n");
        String sprint = "";
        int cont = 0;
        Date dataInicio = null;
        Date dataFim = null;
        for(String item : itens){
            if(item.contains("Total:")){
                break;
            }else if(item.contains("Total features")){
                continue;
            }else if(item.contains("#")){
                sprint = "Sprint " + cont++;
                if(cont == 1){
                    dataInicio = new Date();
                }else{
                    dataInicio = dataFim;
                }
                dataFim = adicionar30dias(dataInicio);
            } else if (item.contains("Feature:")) {
                String feature = item.split(" ")[1];
                taskSeries.add(new Task(feature, dataInicio, dataFim));
            }
        }

        TaskSeriesCollection dataset = new TaskSeriesCollection();
        dataset.add(taskSeries);

        return dataset;
    }

    //Método que adiciona 30 dias a uma data.
    //Utilizado para calcular a quantidade de dias de uma sprint (30 dias)
    private static Date adicionar30dias(Date data){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        return calendar.getTime();
    }

    //Converte o gráfico para imagem base64 para enviar para a tela
    public static String generateChartBase64(JFreeChart chart, int width, int height) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            ChartUtilities.writeChartAsPNG(out, chart, width, height);
            byte[] chartBytes = out.toByteArray();

            // Encode os bytes em base64
            return Base64.getEncoder().encodeToString(chartBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
