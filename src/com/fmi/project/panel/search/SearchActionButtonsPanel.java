package com.fmi.project.panel.search;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.h2.util.StringUtils;

import static org.h2.util.StringUtils.isNullOrEmpty;

import com.fmi.project.MainFrame;
import com.fmi.project.panel.car.CarFormPanel;
import com.fmi.project.panel.owner.OwnerFormPanel;
import com.fmi.project.panel.owner.OwnersTablePanel;
import com.fmi.project.util.DBConnector;
import com.fmi.project.util.MyModel;

@SuppressWarnings("serial")
public class SearchActionButtonsPanel extends JPanel {
	
	Connection conn;
	PreparedStatement state = null;
	
	JButton searchBtn = new JButton("Search");	
	
	public SearchActionButtonsPanel() {	
		searchBtn.addActionListener(new SearchOwnerAction());
		this.add(searchBtn);		
	}
	
	class SearchOwnerAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent event) {
			String firstName = SearchFormPanel.firstNameTField.getText();
			String lastName = SearchFormPanel.lastNameTField.getText();
			String age = SearchFormPanel.ageTField.getText();
			String egn = SearchFormPanel.egnTField.getText();
			String mobileNumber = SearchFormPanel.mobileNumberTField.getText();
			int country = SearchFormPanel.countryCombo.getSelectedIndex();
			String brand = SearchFormPanel.brandTField.getText();
			String carModel = SearchFormPanel.modelTField.getText();
			String creationYear = SearchFormPanel.creationYearTField.getText();
			
					
			String errorMsg = validateFields(firstName,lastName,age,egn,mobileNumber,country,brand,carModel,creationYear);			
			if (!StringUtils.isNullOrEmpty(errorMsg)) {
				MainFrame.setUserMessage(errorMsg,Color.RED);
				return;
			}
			
			String sql = createQuery(firstName,lastName,age,egn,mobileNumber,country,brand,carModel,creationYear);
			
			conn = DBConnector.getConnection();
			try {
				PreparedStatement state = conn.prepareStatement(sql);
				ResultSet result = state.executeQuery();
				MyModel model = new MyModel(result);
				SearchTablePanel.fillTableWithModel(model);
			} catch (SQLException e) {
				System.out.println("Problem with querry : "+ e.getMessage());
			} catch (Exception e) {
				System.out.println("Unexpected error");
			}			
			
			MainFrame.setUserMessage("Searh finished successfully",Color.GREEN);
		}

		private String validateFields(String firstName, String lastName, String age, String egn, String mobileNumber,
				int country, String brand, String carModel, String creationYear) {
			StringBuilder errorMsg = new StringBuilder();
			
			if (isNullOrEmpty(firstName) && isNullOrEmpty(firstName) && isNullOrEmpty(age) && isNullOrEmpty(egn)
					&& isNullOrEmpty(mobileNumber) && isNullOrEmpty(brand) && isNullOrEmpty(carModel)
					&& isNullOrEmpty(creationYear) && country == 0) {
				errorMsg.append("You should choose atleast one criteria for search");
			}

			if (!isNullOrEmpty(firstName)) {
				if(firstName.length() > 20) {
					errorMsg.append("First Name should not be more than 20 characters<br/>");
				}else if(!firstName.matches("[a-zA-Z]+")) {
					errorMsg.append("First Name contains not permitted characters<br/>");
				}
			}
			
			if (!isNullOrEmpty(lastName)) {
				if(lastName.length() > 20) {
					errorMsg.append("Last Name should not be more than 20 characters<br/>");
				}else if(!lastName.matches("[a-zA-Z]+")) {
					errorMsg.append("Last Name contains not permitted characters<br/>");
				}
			}
			
			if (!isNullOrEmpty(age)) {
				if(Integer.valueOf(age) > 120) {
					errorMsg.append("Age can not be more than 120 years<br/>");
				}
			} 
			
			if (!isNullOrEmpty(egn)) {
				if(egn.length() != 10) {
					errorMsg.append("EGN should be 10 characters<br/>");
				}else if (!egn.matches("[0-9]+")) {
					errorMsg.append("EGN should be only numbers<br/>");
				}
			}
			
			if (!isNullOrEmpty(mobileNumber)) {
				if(mobileNumber.length() != 10) {
					errorMsg.append("Mobile Number should be 10 characters<br/>");
				}else if (!mobileNumber.matches("[0-9]+")) {
					errorMsg.append("Mobile Number should be only numbers<br/>");
				}
			} 
			
			if (!isNullOrEmpty(brand)) {
				if(brand.length() > 10) {
					errorMsg.append("Brand should not be more than 10 characters<br/>");
				}
			}
			
			if (!isNullOrEmpty(carModel)) {
				if(carModel.length() > 20) {
					errorMsg.append("Model should not be more than 20 characters<br/>");
				}
			}
			
			if (!isNullOrEmpty(creationYear)) {
				int yearOfCreation = Integer.valueOf(creationYear);
				
				if (!creationYear.matches("[0-9]+")) {
					errorMsg.append("Creation year should be only numbers<br/>");				
				}else if(!(yearOfCreation > 1950 && yearOfCreation < Calendar.getInstance().get(Calendar.YEAR))) {
					errorMsg.append("Creation year must be between 1950 and "+Calendar.getInstance().get(Calendar.YEAR));
				}	
			}
			
			if (!isNullOrEmpty(errorMsg.toString())) {
				errorMsg.insert(0, "<html>");
				errorMsg.insert(errorMsg.length(), "</html>");
			}
			
			return errorMsg.toString();
		}

		private String createQuery(String firstName, String lastName, String age, String egn, String mobileNumber, int country, String brand, String carModel, String creationYear) {
			StringBuilder sql = new StringBuilder("select OWNER.*,COUNTRY.COUNTRY_NAME from OWNER JOIN COUNTRY ON COUNTRY.COUNTRY_ID = OWNER.COUNTRY_ID ");
									
			if (!(isNullOrEmpty(brand) && isNullOrEmpty(carModel) && isNullOrEmpty(creationYear))) {
				sql.append("JOIN CAR ON CAR.OWNER_ID = OWNER.OWNER_ID ");
			}
			sql.append("WHERE");
			
			if (!isNullOrEmpty(firstName)) {
				sql.append(String.format(" FIRST_NAME = '%s' AND",firstName));
			}
			
			if (!isNullOrEmpty(lastName)) {
				sql.append(String.format(" LAST_NAME = '%s' AND",lastName));
			}
			
			if (!isNullOrEmpty(age)) {
				sql.append(String.format(" AGE = %s AND",age));
			}
			
			if (!isNullOrEmpty(egn)) {
				sql.append(String.format(" EGN = '%s' AND",egn));
			}
			
			if (!isNullOrEmpty(mobileNumber)) {
				sql.append(String.format(" MOBILE_NUMBER = '%s' AND",mobileNumber));
			}
			
			if (country != 0) {
				sql.append(String.format(" COUNTRY_ID = %s AND",country));
			}
			
			if (!isNullOrEmpty(brand)) {
				sql.append(String.format(" BRAND = '%s' AND",brand));
			}
			
			if (!isNullOrEmpty(carModel)) {
				sql.append(String.format(" MODEL = '%s' AND",carModel));
			}
			
			if (!isNullOrEmpty(creationYear)) {
				sql.append(String.format(" CREATION_YEAR = '%s' AND",creationYear));
			}
			
			sql.delete(sql.length()-3,sql.length());
			return sql.toString();
		}
		
	}
}
