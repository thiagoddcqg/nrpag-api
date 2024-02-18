/**
 * 
 */
package com.nextreleaseproblem.view;

import com.nextreleaseproblem.model.GeradorParametrosPadrao;
import com.nextreleaseproblem.model.GeradorParametros;
import com.nextreleaseproblem.model.parametros.AlgoritmoEnum;
import com.nextreleaseproblem.model.parametros.ParametrosIteracaoPadrao;
import com.nextreleaseproblem.model.parametros.ParametrosInteracao;
import com.nextreleaseproblem.controller.ExecutorController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Locale;
import java.awt.event.ActionListener;

public class QuadroParametros extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JSpinner numeroSemanaSprint;
	private JSpinner horaPorSemanaSprint;
	private JSpinner numeroSprintFeatures;
	private JSpinner numeroEmpregadosSpring;
	private JSpinner numeroHabilidadesSprint;
	private JSpinner taxaPrecedenciaSprint;
	private JButton botaoLancamento;
	private JComboBox<AlgoritmoEnum> caixaCombinacao;
	
	private ExecutorController controller;

	public QuadroParametros(ExecutorController controller) {
		super("Execute Algorithm");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.controller = controller;
		
		initializeComponents();
		initializeValues();
		
		pack();
	}
	
	private void initializeValues() {
		horaPorSemanaSprint.setValue(ParametrosIteracaoPadrao.HORAS_DA_SEMANA);
		numeroSemanaSprint.setValue(ParametrosIteracaoPadrao.NUMERO_DA_SEMANA);
		numeroSprintFeatures.setValue(GeradorParametrosPadrao.NUMERO_DE_FEATURES);
		numeroEmpregadosSpring.setValue(GeradorParametrosPadrao.NUMERO_DE_EMPREGADOS);
		numeroHabilidadesSprint.setValue(GeradorParametrosPadrao.NUMERO_DE_HABILIDADES);
		taxaPrecedenciaSprint.setValue(GeradorParametrosPadrao.TAXA_DE_PRECEDENCIA);
	}

	private void initializeComponents() {
		int margin = 20;
		Locale.setDefault(Locale.Category.FORMAT, Locale.ENGLISH);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(margin, margin, margin, margin));
		setContentPane(mainPanel);
		setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		mainPanel.add(new JLabel("Algoritmo: "), gbc);
		
		gbc.gridx++;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.LINE_START;
		caixaCombinacao = new JComboBox<>(AlgoritmoEnum.values());
		mainPanel.add(caixaCombinacao, gbc);
		
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		mainPanel.add(new JLabel("INTERAÇÃO"), gbc);
		
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		mainPanel.add(new JLabel("Número de semanas: "), gbc);
		
		gbc.gridx++;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.LINE_START;
		numeroSemanaSprint = new JSpinner(getNovoPositivoIntegerModelo());
		aumentarTamanhoCampoTexto(numeroSemanaSprint);
		mainPanel.add(numeroSemanaSprint, gbc);
		
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		mainPanel.add(new JLabel("Horas por semana: "), gbc);
		
		gbc.gridx++;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.LINE_START;
		horaPorSemanaSprint = new JSpinner(new SpinnerNumberModel(1.0, 1.0, 24.0*7, 1.0));
		aumentarTamanhoCampoTexto(horaPorSemanaSprint);
		mainPanel.add(horaPorSemanaSprint, gbc);
		
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		mainPanel.add(new JLabel("CASE DE TESTE"), gbc);
		
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		mainPanel.add(new JLabel("Numero de features: "), gbc);
		
		gbc.gridx++;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.LINE_START;
		numeroSprintFeatures = new JSpinner(getNovoPositivoIntegerModelo());
		aumentarTamanhoCampoTexto(numeroSprintFeatures);
		mainPanel.add(numeroSprintFeatures, gbc);
		
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		mainPanel.add(new JLabel("Numero de empregados: "), gbc);
		
		gbc.gridx++;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.LINE_START;
		numeroEmpregadosSpring = new JSpinner(getNovoPositivoIntegerModelo());
		aumentarTamanhoCampoTexto(numeroEmpregadosSpring);
		mainPanel.add(numeroEmpregadosSpring, gbc);
		
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		mainPanel.add(new JLabel("Numero de habilidades: "), gbc);
		
		gbc.gridx++;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.LINE_START;
		numeroHabilidadesSprint = new JSpinner(getNovoPositivoIntegerModelo());
		aumentarTamanhoCampoTexto(numeroHabilidadesSprint);
		mainPanel.add(numeroHabilidadesSprint, gbc);
		
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		mainPanel.add(new JLabel("Taxa de porcentagem: "), gbc);
		
		gbc.gridx++;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.LINE_START;
		taxaPrecedenciaSprint = new JSpinner(new SpinnerNumberModel(0.1, 0.0, 1.0, 0.1));
		aumentarTamanhoCampoTexto(taxaPrecedenciaSprint);
		mainPanel.add(taxaPrecedenciaSprint, gbc);
		
		gbc.gridx = 1;
		gbc.gridy++;
		gbc.gridwidth = 1;
		botaoLancamento = new JButton("Lançar");
		getRootPane().setDefaultButton(botaoLancamento);
		botaoLancamento.addActionListener(this);
		botaoLancamento.setFocusPainted(false);
		mainPanel.add(botaoLancamento, gbc);
	}
    
    private void aumentarTamanhoCampoTexto(JSpinner spinner) {
    	JComponent editor = spinner.getEditor();
    	JFormattedTextField ftf = ((JSpinner.DefaultEditor)editor).getTextField();
    	ftf.setColumns(6);
    }
    
    private SpinnerNumberModel getNovoPositivoIntegerModelo() {
    	SpinnerNumberModel modelo = new SpinnerNumberModel();
    	modelo.setMinimum(1);
    	return modelo;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == botaoLancamento) {
			int numeroFeatures = (int) numeroSprintFeatures.getValue(),
				numeroEmpregados = (int) numeroEmpregadosSpring.getValue(),
				numeroHabilidades = (int) numeroHabilidadesSprint.getValue(),
				numeroSemanas = (int) numeroSemanaSprint.getValue();

			double taxaPrecedencia = (double) taxaPrecedenciaSprint.getValue(),
				horasPorSemana = (double) horaPorSemanaSprint.getValue();
			
			GeradorParametros genParam = new GeradorParametros(numeroFeatures, numeroEmpregados,
					numeroHabilidades, taxaPrecedencia);
			ParametrosInteracao iterationParam = new ParametrosInteracao(numeroSemanas, horasPorSemana);
			controller.launch((AlgoritmoEnum) caixaCombinacao.getSelectedItem(), genParam, iterationParam);
		}
	}
}
