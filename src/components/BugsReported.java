package components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class BugsReported extends JPanel {
	Vector data;
	Vector columns;
	DefaultTableModel model;

	public BugsReported() {
		Color backgroundColor = new Color(242, 243, 244);
		setBackground(backgroundColor);
		Color btnColor = new Color(52, 132, 240);

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		data = new Vector();
		columns = new Vector();

		String dummyData[][] = { { "101", "Amit", "Bug 1", "0", "aawdsadwdasdsadwdsadwdasd", "Done" },
				{ "102", "Jai", "Bug 2", "1", "awdsadwdsfsvsvfdfgrfdgdr", "Pending" },
				{ "103", "Sachin", "Bug 3", "2", "uiymhjgjyjhghghtftfhyubnbvnft", "Pending" } };
		String dummyColumns[] = { "Id", "Name", "Title", "Priority", "Description", "Status" };
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
		table.setAutoCreateRowSorter(true);
		table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		add(new JScrollPane(table), gbc);

//		Remove User Button
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 0;
		JButton viewButton = new JButton("View");
		viewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if (table.getSelectedRow() != -1) {
					int row = table.getSelectedRow();
					String dataString = rowDataToString(row);
					JOptionPane.showMessageDialog(null, dataString);
				}
			}
		});
		viewButton.setFocusPainted(false);
		viewButton.setForeground(Color.WHITE);
		viewButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		viewButton.setBackground(btnColor);
		add(viewButton, gbc);

//		Toggle Button
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		JButton completedButton = new JButton("Mark as Completed/Pending");
		completedButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				// check for selected row first
				if (table.getSelectedRow() != -1) {
					// remove selected row from the model
					int rowSelect = table.getSelectedRow();
					String value = (String) model.getValueAt(rowSelect, 5);
					String status = value.equals("Pending") ? "Done" : "Pending";
					int a = JOptionPane.showConfirmDialog(null, "Are you sure you want to mark as " + status + " ?");
					if (a == JOptionPane.YES_OPTION) {
						// remove selected row from the model
						model.setValueAt(status, rowSelect, 5);
					}
				}
			}
		});
		completedButton.setFocusPainted(false);
		completedButton.setForeground(Color.WHITE);
		completedButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		completedButton.setBackground(btnColor);
		add(completedButton, gbc);

		setBorder(new EmptyBorder(10, 10, 10, 10));
		setPreferredSize(new Dimension(585, 600));
	}

	protected String rowDataToString(int row) {
		String dataString = "";
		for (int i = 0; i < model.getColumnCount(); i++) {
			dataString = dataString + model.getColumnName(i) + " : " + model.getValueAt(row, i) + "\n";
		}
		return dataString;
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
