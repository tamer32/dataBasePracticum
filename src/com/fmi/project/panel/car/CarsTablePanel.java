package com.fmi.project.panel.car;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import com.fmi.project.util.DBConnector;
import com.fmi.project.util.MyModel;



@SuppressWarnings("serial")
public class CarsTablePanel extends JPanel {
	
	static int id = -1;
	
	static JTable table = new JTable();
	JScrollPane scroller = new JScrollPane(table);
	
	public CarsTablePanel() {
		this.add(scroller);
		scroller.setPreferredSize(new Dimension(550, 150));
		fillTableWithModel();		
		table.addMouseListener(new MouseTableAction());
	}

	public static void fillTableWithModel() {
		table.setModel(DBConnector.getAllModel("CAR"));
		TableColumnModel tcm = table.getColumnModel();
		tcm.removeColumn(tcm.getColumn(0));
		tcm.removeColumn(tcm.getColumn(3));
	}
	
	public static void fillTableWithModel(MyModel model) {
		table.setModel(model);
		TableColumnModel tcm = table.getColumnModel();
		tcm.removeColumn(tcm.getColumn(0));
		tcm.removeColumn(tcm.getColumn(3));
	}
	
	class MouseTableAction implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			int row = table.getSelectedRow();
			id = Integer.parseInt(table.getModel().getValueAt(row, 0).toString());
			CarFormPanel.brandTField.setText(table.getModel().getValueAt(row, 1).toString());
			CarFormPanel.modelTField.setText(table.getModel().getValueAt(row, 2).toString());
			CarFormPanel.creationYearTField.setText(table.getModel().getValueAt(row, 3).toString());
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
