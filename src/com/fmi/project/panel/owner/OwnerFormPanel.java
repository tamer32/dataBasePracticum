package com.fmi.project.panel.owner;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.fmi.project.util.DBConnector;

@SuppressWarnings("serial")
public class OwnerFormPanel extends JPanel {	
	
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
	
	public OwnerFormPanel() {		
		this.setLayout(new GridLayout(6, 2));
		this.add(firstNameLabel);
		this.add(firstNameTField);
		this.add(lastNameLabel);
		this.add(lastNameTField);
		this.add(ageLabel);
		this.add(ageTField);
		this.add(egnLabel);
		this.add(egnTField);
		this.add(mobileNumberLabel);
		this.add(mobileNumberTField);
		this.add(countryLabel);
		this.add(countryCombo);
		prepareCountryCombo();
	}
	
	public static String[] prepareCountryCombo() {
		ArrayList<String> countries = DBConnector.getAllValuesInColumn("COUNTRY_NAME", "COUNTRY");
		countries.add(0,"");
		countryCombo.setModel(new DefaultComboBoxModel<>(countries.toArray(new String[countries.size()])));
		return countries.toArray(new String[countries.size()]);		
	}
	
	static void clearForm() {
		firstNameTField.setText("");
		lastNameTField.setText("");
		ageTField.setText("");
		egnTField.setText("");
		mobileNumberTField.setText("");
		countryCombo.setSelectedIndex(0);
	}
}
