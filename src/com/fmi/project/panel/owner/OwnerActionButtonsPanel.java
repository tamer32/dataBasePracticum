package com.fmi.project.panel.owner;

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

@SuppressWarnings("serial")
public class OwnerActionButtonsPanel extends JPanel {
	
	Connection conn;
	PreparedStatement state = null;
	
	JButton addBtn = new JButton("Add");
	JButton deleteBtn = new JButton("Delete");
	JButton updateBtn = new JButton("Update");
	
	public OwnerActionButtonsPanel() {
		addBtn.addActionListener(new AddOwnerAction());
		deleteBtn.addActionListener(new DeleteOwnerAction());
		updateBtn.addActionListener(new UpdateOwnerAction());
		this.add(addBtn);
		this.add(deleteBtn);
		this.add(updateBtn);
	}
	
	public class AddOwnerAction implements ActionListener{		

		@Override
		public void actionPerformed(ActionEvent e) {
			String firstName = OwnerFormPanel.firstNameTField.getText();
			String lastName = OwnerFormPanel.lastNameTField.getText();
			String age = OwnerFormPanel.ageTField.getText();
			String egn = OwnerFormPanel.egnTField.getText();
			String mobileNumber = OwnerFormPanel.mobileNumberTField.getText();			
			String countryName = (String) OwnerFormPanel.countryCombo.getSelectedItem();
			String countryId = DBConnector.getAValueFromRowWhere("COUNTRY_ID", "COUNTRY", "COUNTRY_NAME", countryName);
			
			int countryIdAsNum = 0;
			if (!StringUtils.isNullOrEmpty(countryId)) {
				countryIdAsNum = Integer.valueOf(countryId);
			}
			
			String errorMsg = validateForm(firstName,lastName,age,egn,mobileNumber,countryIdAsNum);
			if (!StringUtils.isNullOrEmpty(errorMsg)) {
				MainFrame.setUserMessage(errorMsg,Color.RED);
				return;
			}
			
			String sql = "insert into OWNER values (null,?,?,?,?,?,?);";
			
			conn = DBConnector.getConnection();
			
			try {
				state = conn.prepareStatement(sql);
				state.setString(1, firstName);
				state.setString(2, lastName);
				state.setInt(3, Integer.parseInt(age));
				state.setString(4, mobileNumber);
				state.setString(5, egn);
				state.setInt(6, countryIdAsNum);
				state.execute();
				OwnersTablePanel.fillTableWithModel();
			} catch (SQLException e1) {
				e1.printStackTrace();
				MainFrame.setUserMessage("Owner creation failed",Color.RED);
			}finally {
				try {
					state.close();
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
					MainFrame.setUserMessage("Owner creation failed",Color.RED);
				}
			}			
			OwnerFormPanel.clearForm();
			MainFrame.setUserMessage("Owner creation successfull",Color.GREEN);
		}

		private String validateForm(String firstName, String lastName, String age, String egn, String mobileNumber,
				int country) {
			StringBuilder errorMsg = new StringBuilder();
			
			if (StringUtils.isNullOrEmpty(firstName)) {
				errorMsg.append("First Name should not be empty<br/>");
			}else if(firstName.length() > 20) {
				errorMsg.append("First Name should not be more than 20 characters<br/>");
			}else if(!firstName.matches("[a-zA-Z]+")) {
				errorMsg.append("First Name contains not permitted characters<br/>");
			}
			
			if (StringUtils.isNullOrEmpty(lastName)) {
				errorMsg.append("Last Name should not be empty<br/>");
			}else if(lastName.length() > 20) {
				errorMsg.append("Last Name should not be more than 20 characters<br/>");
			}else if(!lastName.matches("[a-zA-Z]+")) {
				errorMsg.append("Last Name contains not permitted characters<br/>");
			}
			
			if (StringUtils.isNullOrEmpty(age)) {
				errorMsg.append("Age should not be empty<br/>");
			}else if(Integer.valueOf(age) > 120) {
				errorMsg.append("Age can not be more than 120 years<br/>");
			}
			
			if (StringUtils.isNullOrEmpty(egn)) {
				errorMsg.append("EGN should not be empty<br/>");
			}else if(egn.length() != 10) {
				errorMsg.append("EGN should be 10 characters<br/>");
			}else if (!egn.matches("[0-9]+")) {
				errorMsg.append("EGN should be only numbers<br/>");
			}
			
			if (StringUtils.isNullOrEmpty(mobileNumber)) {
				errorMsg.append("Mobile Number should not be empty<br/>");
			}else if(mobileNumber.length() != 10) {
				errorMsg.append("Mobile Number should be 10 characters<br/>");
			}else if (!mobileNumber.matches("[0-9]+")) {
				errorMsg.append("Mobile Number should be only numbers<br/>");
			}
			
			if (country == 0) {
				errorMsg.append("You should choose country");
			}
			
			if (!StringUtils.isNullOrEmpty(errorMsg.toString())) {
				errorMsg.insert(0, "<html>");
				errorMsg.insert(errorMsg.length(), "</html>");
			}
			
			return errorMsg.toString();
		}
		
	}
	
	class DeleteOwnerAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String sql = "delete from OWNER where OWNER_ID=?";
			conn = DBConnector.getConnection();
			try {
				state = conn.prepareStatement(sql);
				state.setInt(1, OwnersTablePanel.id);
				state.execute();
				OwnersTablePanel.id = -1;
				OwnersTablePanel.fillTableWithModel();				
			} catch (SQLException e1) {
				e1.printStackTrace();
				MainFrame.setUserMessage("Owner deletion failed",Color.RED);
			}finally {
				try {
					state.close();
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
					MainFrame.setUserMessage("Owner deletion failed",Color.RED);
				}
			}
			OwnerFormPanel.clearForm();
			MainFrame.setUserMessage("Owner removal successfull",Color.GREEN);
		}
	}
	
	class UpdateOwnerAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			int ownerId = OwnersTablePanel.id;
			String firstName = OwnerFormPanel.firstNameTField.getText();
			String lastName = OwnerFormPanel.lastNameTField.getText();
			String age = OwnerFormPanel.ageTField.getText();
			String egn = OwnerFormPanel.egnTField.getText();
			String mobileNumber = OwnerFormPanel.mobileNumberTField.getText();			
			String countryName = (String) OwnerFormPanel.countryCombo.getSelectedItem();
			String countryId = DBConnector.getAValueFromRowWhere("COUNTRY_ID", "COUNTRY", "COUNTRY_NAME", countryName);
			
			int countryIdAsNum = 0;
			if (!StringUtils.isNullOrEmpty(countryId)) {
				countryIdAsNum = Integer.valueOf(countryId);
			}
			
			String errorMsg = validateForm(ownerId,firstName,lastName,age,egn,mobileNumber,countryIdAsNum);
			if (!StringUtils.isNullOrEmpty(errorMsg)) {
				MainFrame.setUserMessage(errorMsg,Color.RED);
				return;
			}
			
			String sql = "Update OWNER "
					+ "SET FIRST_NAME = ?,LAST_NAME = ?, AGE = ?,MOBILE_NUMBER = ?,EGN = ? ,COUNTRY_ID = ?"
					+ "WHERE OWNER_ID = " + ownerId;
			
			conn = DBConnector.getConnection();
			
			try {
				state = conn.prepareStatement(sql);
				state.setString(1, firstName);
				state.setString(2, lastName);
				state.setInt(3, Integer.parseInt(age));
				state.setString(4, mobileNumber);
				state.setString(5, egn);
				state.setInt(6, countryIdAsNum);
				state.execute();
				OwnersTablePanel.fillTableWithModel();
			} catch (SQLException e1) {
				e1.printStackTrace();
				MainFrame.setUserMessage("Owner creation failed",Color.RED);
			}finally {
				try {
					state.close();
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
					MainFrame.setUserMessage("Owner creation failed",Color.RED);
				}
			}			
			OwnerFormPanel.clearForm();
			MainFrame.setUserMessage("Owner updated successfull",Color.GREEN);
		}

		private String validateForm(int ownerId,String firstName, String lastName, String age, String egn, String mobileNumber,
				int country) {
			StringBuilder errorMsg = new StringBuilder();
			
			if (ownerId == -1) {
				errorMsg.append("You have not selected an Owner from the table<br/>");
			}
			
			if (StringUtils.isNullOrEmpty(firstName)) {
				errorMsg.append("First Name should not be empty<br/>");
			}else if(firstName.length() > 20) {
				errorMsg.append("First Name should not be more than 20 characters<br/>");
			}else if(!firstName.matches("[a-zA-Z]+")) {
				errorMsg.append("First Name contains not permitted characters<br/>");
			}
			
			if (StringUtils.isNullOrEmpty(lastName)) {
				errorMsg.append("Last Name should not be empty<br/>");
			}else if(lastName.length() > 20) {
				errorMsg.append("Last Name should not be more than 20 characters<br/>");
			}else if(!lastName.matches("[a-zA-Z]+")) {
				errorMsg.append("Last Name contains not permitted characters<br/>");
			}
			
			if (StringUtils.isNullOrEmpty(age)) {
				errorMsg.append("Age should not be empty<br/>");
			}else if(Integer.valueOf(age) > 120) {
				errorMsg.append("Age can not be more than 120 years<br/>");
			}
			
			if (StringUtils.isNullOrEmpty(egn)) {
				errorMsg.append("EGN should not be empty<br/>");
			}else if(egn.length() != 10) {
				errorMsg.append("EGN should be 10 characters<br/>");
			}else if (!egn.matches("[0-9]+")) {
				errorMsg.append("EGN should be only numbers<br/>");
			}
			
			if (StringUtils.isNullOrEmpty(mobileNumber)) {
				errorMsg.append("Mobile Number should not be empty<br/>");
			}else if(mobileNumber.length() != 10) {
				errorMsg.append("Mobile Number should be 10 characters<br/>");
			}else if (!mobileNumber.matches("[0-9]+")) {
				errorMsg.append("Mobile Number should be only numbers<br/>");
			}
			
			if (country == 0) {
				errorMsg.append("You should choose country");
			}
			
			if (!StringUtils.isNullOrEmpty(errorMsg.toString())) {
				errorMsg.insert(0, "<html>");
				errorMsg.insert(errorMsg.length(), "</html>");
			}
			
			return errorMsg.toString();
		}
	}
	
}
