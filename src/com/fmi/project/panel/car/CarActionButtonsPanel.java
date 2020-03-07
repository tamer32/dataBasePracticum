package com.fmi.project.panel.car;

import static org.h2.util.StringUtils.isNullOrEmpty;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.h2.util.StringUtils;

import com.fmi.project.MainFrame;
import com.fmi.project.panel.owner.OwnersTablePanel;
import com.fmi.project.util.DBConnector;
import com.fmi.project.util.MyModel;

@SuppressWarnings("serial")
public class CarActionButtonsPanel extends JPanel {
	
	Connection conn;
	PreparedStatement state = null;
	
	public JButton addBtn = new JButton("Add");
	public JButton updateBtn = new JButton("Update");
	public JButton deleteBtn = new JButton("Delete");	
	public JButton searchBtn = new JButton("Search");
	
	public CarActionButtonsPanel() {
		
		searchBtn.addActionListener(new SearchCarAction());
		this.add(searchBtn);
		
		addBtn.addActionListener(new AddCarAction());
		addBtn.setVisible(false);
		this.add(addBtn);
		
		updateBtn.addActionListener(new UpdateCarAction());
		this.add(updateBtn);
		
		deleteBtn.addActionListener(new DeleteCarAction());		
		this.add(deleteBtn);	
		
	}
	
	public class AddCarAction implements ActionListener{		

		@Override
		public void actionPerformed(ActionEvent e) {
			String brand = CarFormPanel.brandTField.getText();
			String model = CarFormPanel.modelTField.getText();
			String creationYear = CarFormPanel.creationYearTField.getText();
			int owner = OwnersTablePanel.id;
			
			if (owner == -1) {
				MainFrame.setUserMessage("You should choose owner", Color.RED);
				return;
			}
			
			String sql = "insert into CAR values (null,?,?,?,?);";
			
			conn = DBConnector.getConnection();
			
			try {
				state = conn.prepareStatement(sql);
				state.setString(1, brand);
				state.setString(2, model);
				state.setString(3, creationYear);
				state.setInt(4, owner);
				state.execute();
				CarsTablePanel.fillTableWithModel();
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
			CarFormPanel.clearForm();
			addBtn.setVisible(false);
			MainFrame.homeBtn.doClick();
			MainFrame.carBtn.doClick();
			MainFrame.setUserMessage("Car creation successfull",Color.GREEN);
		}
		
	}
	
	public class SearchCarAction implements ActionListener{		

		@Override
		public void actionPerformed(ActionEvent e) {
			String brand = CarFormPanel.brandTField.getText();
			String model = CarFormPanel.modelTField.getText();
			String creationYear = CarFormPanel.creationYearTField.getText();			
			
			
			String errorMsg = validateForm(brand,model,creationYear);
			if (!StringUtils.isNullOrEmpty(errorMsg)) {
				MainFrame.setUserMessage(errorMsg,Color.RED);
				return;
			}
			
			String sql = createQuery(brand,model,creationYear);
			conn = DBConnector.getConnection();
			
			try {
				state = conn.prepareStatement(sql);				
				try {
					CarsTablePanel.fillTableWithModel(new MyModel(state.executeQuery()));
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
			MainFrame.setUserMessage("Car search successfull",Color.GREEN);
		}

		private String validateForm(String brand, String model, String creationYear) {
			StringBuilder errorMsg = new StringBuilder();
			
			if (!StringUtils.isNullOrEmpty(brand)) {
				if(brand.length() > 10) {
					errorMsg.append("Brand should not be more than 10 characters<br/>");
				}
			}
			
			if (!StringUtils.isNullOrEmpty(model)) {
				if(model.length() > 20) {
					errorMsg.append("Model should not be more than 20 characters<br/>");
				}
			}
			
			if (!StringUtils.isNullOrEmpty(creationYear)) {
				int yearOfCreation = Integer.valueOf(creationYear);
				
				if (!creationYear.matches("[0-9]+")) {
					errorMsg.append("Creation year should be only numbers<br/>");				
				}else if(!(yearOfCreation > 1950 && yearOfCreation < Calendar.getInstance().get(Calendar.YEAR))) {
					errorMsg.append("Creation year must be between 1950 and "+Calendar.getInstance().get(Calendar.YEAR));
				}	
			}
			
			if (!StringUtils.isNullOrEmpty(errorMsg.toString())) {
				errorMsg.insert(0, "<html>");
				errorMsg.insert(errorMsg.length(), "</html>");
			}
			
			return errorMsg.toString();
		}

		private String createQuery(String brand, String model, String creationYear) {
			StringBuilder sql = new StringBuilder("select * from CAR where ");
			
			if (!StringUtils.isNullOrEmpty(brand)) {
				sql.append(String.format(" brand = '%s' and",brand));
			}
			
			if (!StringUtils.isNullOrEmpty(model)) {
				sql.append(String.format(" model = '%s' and",model));
			}
			
			if (!StringUtils.isNullOrEmpty(creationYear)) {
				sql.append(String.format(" creation_year = '%s' and",creationYear));
			}
			
			sql.delete(sql.length()-3,sql.length());
			return sql.toString();
		}
		
	}
	
	class DeleteCarAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String sql = "delete from CAR where CAR_ID=?";
			conn = DBConnector.getConnection();
			try {
				state = conn.prepareStatement(sql);
				state.setInt(1, CarsTablePanel.id);
				state.execute();
				CarsTablePanel.id = -1;
				CarsTablePanel.fillTableWithModel();
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
			CarFormPanel.clearForm();
			MainFrame.setUserMessage("Car removal successfull",Color.GREEN);
		}
	}
	
	public class UpdateCarAction implements ActionListener{		

		@Override
		public void actionPerformed(ActionEvent e) {
			int carId =CarsTablePanel.id;
			String brand = CarFormPanel.brandTField.getText();
			String model = CarFormPanel.modelTField.getText();
			String creationYear = CarFormPanel.creationYearTField.getText();
			int owner = OwnersTablePanel.id;			
			
			String errorMsg = validateForm(brand,model,creationYear,owner);			
			if (!StringUtils.isNullOrEmpty(errorMsg)) {
				MainFrame.setUserMessage(errorMsg,Color.RED);
				return;
			}
			
			String sql = "UPDATE CAR "
					+ "SET BRAND = ?,MODEL = ?,CREATION_YEAR = ?, OWNER_ID = ? "
					+ "WHERE CAR_ID = "+carId;
			
			conn = DBConnector.getConnection();
			
			try {
				state = conn.prepareStatement(sql);
				state.setString(1, brand);
				state.setString(2, model);
				state.setString(3, creationYear);
				state.setInt(4, owner);
				state.execute();
				CarsTablePanel.fillTableWithModel();
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
			CarFormPanel.clearForm();
			addBtn.setVisible(false);
			MainFrame.homeBtn.doClick();
			MainFrame.carBtn.doClick();
			MainFrame.setUserMessage("Car update successfull",Color.GREEN);
		}

		private String validateForm(String brand, String model, String creationYear, int owner) {
			StringBuilder errorMsg = new StringBuilder();			
			
			if (!isNullOrEmpty(brand)) {
				if(brand.length() > 10) {
					errorMsg.append("Brand should not be more than 10 characters<br/>");
				}
			}
			
			if (!isNullOrEmpty(model)) {
				if(model.length() > 20) {
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
		
	}
	
}
