package com.fmi.project.panel.search;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import com.fmi.project.util.MyModel;



@SuppressWarnings("serial")
public class SearchTablePanel extends JPanel {	
	
	static JTable table = new JTable();
	JScrollPane scroller = new JScrollPane(table);
	
	public SearchTablePanel() {
		this.add(scroller);
		scroller.setPreferredSize(new Dimension(550, 150));
	}

	public static void fillTableWithModel(MyModel model) {
		table.setModel(model);
		TableColumnModel tcm = table.getColumnModel();
		tcm.removeColumn(tcm.getColumn(0));
		tcm.removeColumn(tcm.getColumn(5));
	}
	
		
}
