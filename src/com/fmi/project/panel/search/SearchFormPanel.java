package com.fmi.project.panel.search;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.fmi.project.panel.owner.OwnerFormPanel;

public class SearchFormPanel extends JPanel {
		
	//Car
	JLabel brandLabel = new JLabel("Brand:");
	JLabel modelLabel = new JLabel("Model:");
	JLabel creationYearLabel = new JLabel("Creation year:");
	
	static JTextField brandTField = new JTextField();
	static JTextField modelTField = new JTextField();
	static JTextField creationYearTField = new JTextField();	
	
	//Owner
	JLabel firstNameLabel = new JLabel("First Name:");
	JLabel lastNameLabel = new JLabel("Last Name:");
	JLabel ageLabel = new JLabel("Age:");
	JLabel egnLabel = new JLabel("EGN:");
	JLabel mobileNumberLabel = new JLabel("Mobile Number:");
	JLabel countryLabel = new JLabel("Country:");
	
	static JTextField firstNameTField = new JTextField();
	static JTextField lastNameTField = new JTextField();
	static JTextField ageTField = new JTextField();
	static JTextField egnTField = new JTextField();
	static JTextField mobileNumberTField = new JTextField();	
	static JComboBox<String> countryCombo = new JComboBox<>();
	
	public SearchFormPanel() {
		this.setPreferredSize(new Dimension(550, 125));		
		this.setLayout(new GridLayout(6, 4));
		
		this.add(firstNameLabel);
		this.add(firstNameTField);
		this.add(brandLabel);
		this.add(brandTField);
		
		this.add(lastNameLabel);
		this.add(lastNameTField);
		this.add(modelLabel);
		this.add(modelTField);
				
		this.add(ageLabel);
		this.add(ageTField);
		this.add(creationYearLabel);
		this.add(creationYearTField);
		
		this.add(egnLabel);
		this.add(egnTField);
		this.add(new JLabel(""));
		this.add(new JLabel(""));
		
		this.add(mobileNumberLabel);
		this.add(mobileNumberTField);
		this.add(new JLabel(""));
		this.add(new JLabel(""));
		
		this.add(countryLabel);
		this.add(countryCombo);
		this.add(new JLabel(""));
		this.add(new JLabel(""));
		prepareComboBox();
	}	
	
	public static void prepareComboBox() {
		countryCombo.setModel(new DefaultComboBoxModel<>(OwnerFormPanel.prepareCountryCombo()));
	}
}
