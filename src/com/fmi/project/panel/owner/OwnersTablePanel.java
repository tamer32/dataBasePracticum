package com.fmi.project.panel.owner;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import com.fmi.project.util.DBConnector;



@SuppressWarnings("serial")
public class OwnersTablePanel extends JPanel {
	
	public static int id = -1;
	
	static JTable table = new JTable();
	JScrollPane scroller = new JScrollPane(table);
	
	public OwnersTablePanel() {
		this.add(scroller);
		scroller.setPreferredSize(new Dimension(550, 150));
		fillTableWithModel();		
		table.addMouseListener(new MouseTableAction());
	}

	public static void fillTableWithModel() {
		table.setModel(DBConnector.getAllModel("OWNER JOIN COUNTRY ON COUNTRY.COUNTRY_ID = OWNER.COUNTRY_ID "));
		TableColumnModel tcm = table.getColumnModel();
		tcm.removeColumn(tcm.getColumn(0));
		tcm.removeColumn(tcm.getColumn(5));
		tcm.removeColumn(tcm.getColumn(5));
		tcm.removeColumn(tcm.getColumn(5));
	}
	
	class MouseTableAction implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			int row = table.getSelectedRow();
			id = Integer.parseInt(table.getModel().getValueAt(row, 0).toString());
			OwnerFormPanel.firstNameTField.setText(table.getModel().getValueAt(row, 1).toString());
			OwnerFormPanel.lastNameTField.setText(table.getModel().getValueAt(row, 2).toString());
			OwnerFormPanel.ageTField.setText(table.getModel().getValueAt(row, 3).toString());
			OwnerFormPanel.mobileNumberTField.setText(table.getModel().getValueAt(row, 4).toString());
			OwnerFormPanel.egnTField.setText(table.getModel().getValueAt(row, 5).toString());
			OwnerFormPanel.countryCombo.setSelectedIndex(Integer.valueOf(table.getModel().getValueAt(row, 6).toString()));
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
