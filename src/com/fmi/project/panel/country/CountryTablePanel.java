package com.fmi.project.panel.country;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import com.fmi.project.panel.car.CarFormPanel;
import com.fmi.project.util.DBConnector;
import com.fmi.project.util.MyModel;

public class CountryTablePanel extends JPanel {
	
	static int id = -1;
	
	static JTable table = new JTable();
	JScrollPane scroller = new JScrollPane(table);
	
	public CountryTablePanel() {
		this.add(scroller);
		scroller.setPreferredSize(new Dimension(550, 150));
		fillTableWithModel();		
		table.addMouseListener(new MouseTableAction());
	}

	public static void fillTableWithModel() {
		table.setModel(DBConnector.getAllModel("COUNTRY"));
		TableColumnModel tcm = table.getColumnModel();
		tcm.removeColumn(tcm.getColumn(0));		
	}
	
	public static void fillTableWithModel(MyModel model) {
		table.setModel(model);
		TableColumnModel tcm = table.getColumnModel();
		tcm.removeColumn(tcm.getColumn(0));		
	}
	
	class MouseTableAction implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			int row = table.getSelectedRow();
			id = Integer.parseInt(table.getModel().getValueAt(row, 0).toString());	
			CountryFormPanel.countryCodeTField.setText(table.getModel().getValueAt(row, 1).toString());
			CountryFormPanel.countryNameTField.setText(table.getModel().getValueAt(row, 2).toString());
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	
		
	}
}
