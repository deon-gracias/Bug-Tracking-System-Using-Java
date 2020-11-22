package components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
	Vector data = new Vector();
	Vector columnHeaders = new Vector();
	DefaultTableModel model;
	Connection conn;
	int userId;
	int projectId;

	public BugsReported(Connection conn, int userId, int projectId) {
		this.userId = userId;
		this.projectId = projectId;
		this.conn = conn;
		Color backgroundColor = new Color(242, 243, 244);
		setBackground(backgroundColor);
		Color btnColor = new Color(52, 132, 240);

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		setData();

//		JTable
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 0;
		model = new DefaultTableModel(this.data, this.columnHeaders);
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
					int id = Integer.parseInt((String) model.getValueAt(rowSelect, 0));
					String value = (String) model.getValueAt(rowSelect, 4);
					String status = value.equals("Pending") ? "Done" : "Pending";
					int a = JOptionPane.showConfirmDialog(null, "Are you sure you want to mark as " + status + " ?");
					if (a == JOptionPane.YES_OPTION) {
						// remove selected row from the model
						updateBugStatus(id, status);
						model.setValueAt(status, rowSelect, 4);
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

	String[][] getData() {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(
					"SELECT id,title,priority,description,status,reportedBy FROM Bugs WHERE reportedBy=? AND pid=? ");
			ps.setInt(1, userId);
			ps.setInt(2, projectId);
			ResultSet rs = ps.executeQuery();

			ArrayList<String[]> listdata = new ArrayList<String[]>();
			int i = 0;
			while (rs.next()) {
				String[] tempdata = new String[6];
				tempdata[0] = rs.getString("id");
				tempdata[1] = rs.getString("title");
				tempdata[2] = rs.getString("priority");
				tempdata[3] = rs.getString("description");
				tempdata[4] = rs.getString("status");
				int uid = rs.getInt("reportedBy");
				ps = conn.prepareStatement("SELECT user_name FROM Users WHERE id=?;");
				ps.setInt(1, uid);
				ResultSet rs1 = ps.executeQuery();
				if (rs1.next()) {
					tempdata[5] = rs1.getString("user_name");
				}
				listdata.add(tempdata);
			}

			String[][] data = new String[listdata.size()][6];
			for (int n = 0; n < listdata.size(); n++) {
				String[] temp = listdata.get(n);
				data[n][0] = temp[0];
				data[n][1] = temp[1];
				data[n][2] = temp[2];
				data[n][3] = temp[3];
				data[n][4] = temp[4];
				data[n][5] = temp[5];
			}
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	protected String rowDataToString(int row) {
		String dataString = "";
		for (int i = 0; i < model.getColumnCount(); i++) {
			dataString = dataString + model.getColumnName(i) + " : " + model.getValueAt(row, i) + "\n";
		}
		return dataString;
	}

	public void setData() {
		String[][] data = getData();
		String cols[] = { "Id", "Title", "Priority", "Description", "Status", "Reported By" };
		for (String[] ele : data) {
			Vector info = new Vector();
			for (String anotherele : ele) {
				info.add(anotherele);
			}
			this.data.add(info);
		}
		for (int i = 0; i < cols.length; i++) {
			this.columnHeaders.add(cols[i]);
		}
	}

	public void dataModified() {
		this.data = model.getDataVector();
	}

	public void updateBugStatus(int id, String status) {
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE Bugs SET status = ? WHERE id=?;");
			ps.setString(1, status);
			ps.setInt(2, id);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
