package com.fmi.project.panel.country;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.h2.util.StringUtils;

import com.fmi.project.MainFrame;
import com.fmi.project.panel.car.CarsTablePanel;
import com.fmi.project.util.DBConnector;
import com.fmi.project.util.MyModel;

public class CountryActionButtonsPanel extends JPanel {
	
	Connection conn;
	PreparedStatement state = null;
	
	public JButton addBtn = new JButton("Add");
	public JButton updateBtn = new JButton("Update");
	public JButton deleteBtn = new JButton("Delete");
	public JButton searchBtn = new JButton("Search");
	
	public CountryActionButtonsPanel() {		
		searchBtn.addActionListener(new SearchCountryAction());		
		this.add(searchBtn);	
		
		addBtn.addActionListener(new AddCountryAction());
		this.add(addBtn);
		
		updateBtn.addActionListener(new UpdateCountryAction());
		this.add(updateBtn);
		
		deleteBtn.addActionListener(new DeleteCountryAction());		
		this.add(deleteBtn);
	}
	
	public class AddCountryAction implements ActionListener{		

		@Override
		public void actionPerformed(ActionEvent e) {
			String countryCode = CountryFormPanel.countryCodeTField.getText();
			String countryName = CountryFormPanel.countryNameTField.getText();			
			
			String errorMsg = validateForm(countryCode,countryName);
			if (!StringUtils.isNullOrEmpty(errorMsg)) {
				MainFrame.setUserMessage(errorMsg,Color.RED);
				return;
			}
			
			String sql = "insert into COUNTRY values (null,?,?);";
			
			conn = DBConnector.getConnection();
			
			try {
				state = conn.prepareStatement(sql);
				state.setString(1, countryCode);
				state.setString(2, countryName);			
				state.execute();
				CountryTablePanel.fillTableWithModel();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}finally {
				try {
					state.close();
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}			
			CountryFormPanel.clearForm();
			MainFrame.setUserMessage("Country creation successfull",Color.GREEN);
		}

		private String validateForm(String countryCode, String countryName) {
			StringBuilder errorMsg = new StringBuilder();
			
			if (StringUtils.isNullOrEmpty(countryCode)) {
				errorMsg.append("Country code should not be empty<br/>");
			}else if(countryCode.length() > 2) {
				errorMsg.append("Country code should not be more than 2 characters<br/>");
			}else if(!countryCode.matches("[a-zA-Z]+")) {
				errorMsg.append("Country code contains not permitted characters<br/>");
			}
			
			if (StringUtils.isNullOrEmpty(countryName)) {
				errorMsg.append("Country Name should not be empty<br/>");
			}else if(countryName.length() > 20) {
				errorMsg.append("Country Name should not be more than 20 characters<br/>");
			}else if(!countryName.matches("[a-z A-Z]+")) {
				errorMsg.append("Country Name contains not permitted characters<br/>");
			}		
			
			
			if (!StringUtils.isNullOrEmpty(errorMsg.toString())) {
				errorMsg.insert(0, "<html>");
				errorMsg.insert(errorMsg.length(), "</html>");
			}
			
			return errorMsg.toString();
		}
		
	}
	
	class DeleteCountryAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String sql = "delete from COUNTRY where COUNTRY_ID=?";
			conn = DBConnector.getConnection();
			try {
				state = conn.prepareStatement(sql);
				state.setInt(1, CountryTablePanel.id);
				state.execute();
				CountryTablePanel.id = -1;
				CountryTablePanel.fillTableWithModel();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}finally {
				try {
					state.close();
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			CountryFormPanel.clearForm();
			MainFrame.setUserMessage("Country removal successfull",Color.GREEN);
		}
	}
	
	class SearchCountryAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String countryCode = CountryFormPanel.countryCodeTField.getText();
			String countryName = CountryFormPanel.countryNameTField.getText();
			
			String errorMsg = validateForm(countryCode,countryName);
			if (!StringUtils.isNullOrEmpty(errorMsg)) {
				MainFrame.setUserMessage(errorMsg,Color.RED);
				return;
			}
			
			String sql = createQuery(countryCode,countryName);
			conn = DBConnector.getConnection();
			try {
				state = conn.prepareStatement(sql);
				state.executeQuery();
				try {
					CountryTablePanel.fillTableWithModel(new MyModel(state.executeQuery()));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}finally {
				try {
					state.close();
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			CountryFormPanel.clearForm();
			MainFrame.setUserMessage("Country search successfull",Color.GREEN);
		}

		private String createQuery(String countryCode, String countryName) {
			StringBuilder sql = new StringBuilder("select * from COUNTRY where");
			
			if (!StringUtils.isNullOrEmpty(countryCode)) {
				sql.append(String.format(" country_code = '%s' and",countryCode));
			}
			
			if (!StringUtils.isNullOrEmpty(countryName)) {
				sql.append(String.format(" country_name = '%s' and",countryName));
			}
			
			sql.delete(sql.length()-3,sql.length());
			
			return sql.toString();
		}
		
		private String validateForm(String countryCode, String countryName) {
			StringBuilder errorMsg = new StringBuilder();
			
			if (!StringUtils.isNullOrEmpty(countryCode)) {
				if(countryCode.length() > 2) {
					errorMsg.append("Country code should not be more than 2 characters<br/>");
				}else if(!countryCode.matches("[a-zA-Z]+")) {
					errorMsg.append("Country code contains not permitted characters<br/>");
				}
			}
						
			if (!StringUtils.isNullOrEmpty(countryName)) {
				if(countryName.length() > 20) {
					errorMsg.append("Country Name should not be more than 20 characters<br/>");
				}else if(!countryName.matches("[a-z A-Z]+")) {
					errorMsg.append("Country Name contains not permitted characters<br/>");
				}	
			}
			
			if (!StringUtils.isNullOrEmpty(errorMsg.toString())) {
				errorMsg.insert(0, "<html>");
				errorMsg.insert(errorMsg.length(), "</html>");
			}
			
			return errorMsg.toString();
		}
	}
	
	public class UpdateCountryAction implements ActionListener{		

		@Override
		public void actionPerformed(ActionEvent e) {
			int countryId =CountryTablePanel.id;
			String countryCode = CountryFormPanel.countryCodeTField.getText();
			String countryName = CountryFormPanel.countryNameTField.getText();
			int owner = CountryTablePanel.id;			
			
			String errorMsg = validateForm(countryCode,countryName);			
			if (!StringUtils.isNullOrEmpty(errorMsg)) {
				MainFrame.setUserMessage(errorMsg,Color.RED);
				return;
			}
			
			String sql = "UPDATE COUNTRY "
					+ "SET COUNTRY_CODE = ?,COUNTRY_NAME = ? "
					+ "WHERE COUNTRY_ID = "+countryId;
			
			conn = DBConnector.getConnection();
			
			try {
				state = conn.prepareStatement(sql);
				state.setString(1, StringUtils.toUpperEnglish(countryCode));
				state.setString(2, countryName);
				state.execute();
				CountryTablePanel.fillTableWithModel();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}finally {
				try {
					state.close();
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}			
			CountryFormPanel.clearForm();
			MainFrame.setUserMessage("Country update successfull",Color.GREEN);
		}

		private String validateForm(String countryCode, String countryName) {
			StringBuilder errorMsg = new StringBuilder();
			
			if (StringUtils.isNullOrEmpty(countryCode)) {
				errorMsg.append("Country code should not be empty<br/>");
			}else if(countryCode.length() > 2) {
				errorMsg.append("Country code should not be more than 2 characters<br/>");
			}else if(!countryCode.matches("[a-zA-Z]+")) {
				errorMsg.append("Country code contains not permitted characters<br/>");
			}
			
			if (StringUtils.isNullOrEmpty(countryName)) {
				errorMsg.append("Country Name should not be empty<br/>");
			}else if(countryName.length() > 20) {
				errorMsg.append("Country Name should not be more than 20 characters<br/>");
			}else if(!countryName.matches("[a-zA-Z]+")) {
				errorMsg.append("Country Name contains not permitted characters<br/>");
			}		
			
			
			if (!StringUtils.isNullOrEmpty(errorMsg.toString())) {
				errorMsg.insert(0, "<html>");
				errorMsg.insert(errorMsg.length(), "</html>");
			}
			
			return errorMsg.toString();
		}		
		
	}
}
