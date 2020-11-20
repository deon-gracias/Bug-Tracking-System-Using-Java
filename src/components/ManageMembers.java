package components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ManageMembers extends JPanel {
	Vector data;
	Vector columns;
	DefaultTableModel model;

	public ManageMembers() {

		Color backgroundColor = new Color(242, 243, 244);
		setBackground(backgroundColor);

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		data = new Vector();
		columns = new Vector();

		String dummyData[][] = { { "101", "Amit", "true", "true", "false" }, { "102", "Jai", "true", "false", "false" },
				{ "103", "Sachin", "false", "true", "false" } };
		String dummyColumns[] = { "Id", "Name", "Admin", "Developer", "Tester" };
		setData(dummyData, dummyColumns);

//		JTable
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 0;
		model = new DefaultTableModel(this.data, this.columns);
		JTable table = new JTable(model) {
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		add(new JScrollPane(table), gbc);

//		Remove User Button
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 0;
		JButton removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				// check for selected row first
				if (table.getSelectedRow() != -1) {
					int a = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove user?");
					if (a == JOptionPane.YES_OPTION) {
						// remove selected row from the model
						model.removeRow(table.getSelectedRow());
					}
				}
			}
		});
		add(removeButton, gbc);

//		Toggle Button
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		JButton toggleButton = new JButton("Toggle");
		toggleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				// check for selected row first
				if (table.getSelectedRow() != -1) {
					// remove selected row from the model
					int rowSelect = table.getSelectedRow();
					int columnSelect = table.getSelectedColumn();
					String value = (String) model.getValueAt(rowSelect, columnSelect);
					if (columnSelect > 1) {
						model.setValueAt(value.equals("true") ? "false" : "true", rowSelect, columnSelect);
					}
					dataModified();
				}
			}
		});
		add(toggleButton, gbc);

		setPreferredSize(new Dimension(585, 600));
	}

	public void setData(String[][] data, String[] cols) {

		for (String[] ele : data) {
			Vector info = new Vector();
			for (String anotherele : ele) {
				info.add(anotherele);
			}
			this.data.add(info);
		}
		for (int i = 0; i < cols.length; i++) {
			this.columns.add(cols[i]);
		}
	}

	public void dataModified() {
		this.data = model.getDataVector();
	}

}
