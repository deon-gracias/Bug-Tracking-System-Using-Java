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
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ManageMembers extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3910207224709691748L;
	Connection conn;
	int userId, projectId;
	Vector data = new Vector();
	Vector columnHeaders = new Vector();
	DefaultTableModel model;

	public ManageMembers(Connection conn, int userId, int projectId) {
		this.conn = conn;
		this.userId = userId;
		this.projectId = projectId;
		Color backgroundColor = new Color(242, 243, 244);
		setBackground(backgroundColor);

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		// Set Data of table
		setData();

		// JTable
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 0;

		// Creating table model and using the data and column headers
		model = new DefaultTableModel(this.data, this.columnHeaders);
		JTable table = new JTable(model) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -3189089986264441360L;

			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		add(new JScrollPane(table), gbc);

		// Add User Button
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 0;
		JButton addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				int uid = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter User Id"));
				try {
					// Insert new users privileges
					PreparedStatement ps = conn.prepareStatement("INSERT INTO Privileges(uid,pid,type) VALUES (?,?,?)");
					ps.setInt(1, uid);
					ps.setInt(2, projectId);
					ps.setString(3, "tester"); // Set by default tester
					// Execute Statement
					ps.executeUpdate();

					// Get User Name
					ps = conn.prepareStatement("SELECT user_name FROM Users WHERE uid = ?;");
					ps.setInt(1, uid);
					ResultSet rs = ps.executeQuery();
					String username = "";
					if (rs.next()) {
						username = rs.getString("user_name");
					}

					// Add User to Table
					model.addRow(new Object[] { uid, username, "false", "false", "true" });
				} catch (Exception e) {
					e.printStackTrace();
				}
				dataModified();
			}

		});

		add(addButton, gbc);

		// Remove User Button
		gbc.gridx = 2;
		gbc.gridy = 2;
		gbc.gridwidth = 0;
		JButton removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				// Check for selected row first
				if (table.getSelectedRow() != -1) {
					int a = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove user?");
					if (a == JOptionPane.YES_OPTION) {
						int rowSelect = table.getSelectedRow();
						int id = Integer.parseInt((String) model.getValueAt(rowSelect, 0));

						// remove selected row from the model
						model.removeRow(table.getSelectedRow());
						try {
							// Removing User from Privileges
							PreparedStatement ps = conn
									.prepareStatement("DELETE FROM Privileges WHERE uid=? and pid=?");
							ps.setInt(1, id);
							ps.setInt(2, projectId);
							ps.executeUpdate();

						} catch (Exception e) {
							e.printStackTrace();
						}
						dataModified();
					}
				}
			}
		});
		add(removeButton, gbc);

		// Toggle Button
		gbc.gridx = 3;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		JButton toggleButton = new JButton("Toggle");
		toggleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				// Check for selected row first
				if (table.getSelectedRow() != -1) {
					// Remove selected row from the model
					int rowSelect = table.getSelectedRow();
					int columnSelect = table.getSelectedColumn();
					String value = (String) model.getValueAt(rowSelect, columnSelect);
					if (columnSelect > 1) {
						model.setValueAt(value.equals("true") ? "false" : "true", rowSelect, columnSelect);
					}
					try {
						PreparedStatement ps;

						if (value.equals("true")) {
							// Remove privileges of user from Database
							ps = conn.prepareStatement(
									"DELETE FROM Privileges WHERE (uid=? and pid=?) and privileges=?");
							ps.setInt(1, Integer.parseInt((String) model.getValueAt(rowSelect, 0)));
							ps.setInt(2, projectId);

						} else {
							// Add privileges of user from Database
							ps = conn.prepareStatement("INSERT INTO Privileges(uid, pid, type) VALUES(?,?,?);");
							ps.setInt(1, Integer.parseInt((String) model.getValueAt(rowSelect, 0)));
							ps.setInt(2, projectId);
						}

						// Get which privileges to delete
						if (columnSelect == 2) {
							ps.setString(3, "admin");
						} else if (columnSelect == 3) {
							ps.setString(3, "developer");
						} else {
							ps.setString(3, "tester");
						}

						ps.executeUpdate();
					} catch (Exception e) {
						e.printStackTrace();
					}
					dataModified();
				}
			}
		});
		add(toggleButton, gbc);

		setPreferredSize(new Dimension(585, 600));
	}

	String[][] getData() {
		PreparedStatement ps;
		try {
			// Get Privileges
			ps = conn.prepareStatement("SELECT uid,type FROM Privileges WHERE pid=?;");
			ps.setInt(1, projectId);
			ResultSet rs = ps.executeQuery();

			HashMap<Integer, String[]> listdata = new HashMap<Integer, String[]>();
			// Storing result in List Data
			while (rs.next()) {
				String[] tempdata = new String[5];
				int uid = rs.getInt("uid");
				tempdata[0] = Integer.toString(uid);
				ps = conn.prepareStatement("SELECT user_name FROM Users WHERE id=?;");
				ps.setInt(1, Integer.parseInt(tempdata[0]));
				ResultSet rs1 = ps.executeQuery();
				if (rs1.next()) {
					tempdata[1] = rs1.getString("user_name");
				}
				String type = rs.getString("type");
				if (!listdata.containsKey(uid)) {
					tempdata[2] = "false";
					tempdata[3] = "false";
					tempdata[4] = "false";
				} else {
					String[] temp = new String[5];
					temp = listdata.get(uid);
					tempdata[2] = temp[2];
					tempdata[3] = temp[3];
					tempdata[4] = temp[4];
				}
				if(!rs.wasNull()){
				if (type.equals("admin")) {
						tempdata[2] = "true";
					} else if (type.equals("developer")) {
						tempdata[3] = "true";
					} else {
						tempdata[4] = "true";
					}
				}

				listdata.put(Integer.parseInt(tempdata[0]), tempdata);
			}

			// Converting List to String[][]
			String[][] data = new String[listdata.size()][5];
			int n = 0;
			for (int i : listdata.keySet()) {
				String[] temp = new String[5];
				temp = listdata.get(i);
				data[n][0] = temp[0];
				data[n][1] = temp[1];
				data[n][2] = temp[2];
				data[n][3] = temp[3];
				data[n][4] = temp[4];
				n++;
			}
			return data;
		} catch (

		SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void setData() {
		// Get data
		String[][] data = getData();

		String cols[] = { "Id", "Name", "Admin", "Developer", "Tester" };

		// Setting Data of Vector
		for (String[] ele : data) {
			Vector info = new Vector();
			for (String anotherele : ele) {
				info.add(anotherele);
			}
			this.data.add(info);
		}

		// Setting columns
		for (int i = 0; i < cols.length; i++) {
			this.columnHeaders.add(cols[i]);
		}
	}

	public void dataModified() {
		this.data = model.getDataVector();
	}

}
