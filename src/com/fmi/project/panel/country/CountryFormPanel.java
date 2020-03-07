package com.fmi.project.panel.country;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CountryFormPanel extends JPanel {
	JLabel countryCodeLabel = new JLabel("Country code:");
	JLabel countryNamelLabel = new JLabel("Country name:");
	
	static JTextField countryCodeTField = new JTextField();
	static JTextField countryNameTField = new JTextField();
	
	public CountryFormPanel() {
		this.setPreferredSize(new Dimension(550, 50));
		this.setLayout(new GridLayout(2, 2));
		this.add(countryCodeLabel);
		this.add(countryCodeTField);
		this.add(countryNamelLabel);
		this.add(countryNameTField);
	}
	
	static void clearForm() {
		countryCodeTField.setText("");
		countryNameTField.setText("");
	}
}
