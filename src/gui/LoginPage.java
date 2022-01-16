package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class LoginPage extends JFrame {
	String username;
	String password;
	int userId, currentPid;
	String[] privileges = { "", "", "" };
	Connection conn;
	HashMap<Integer, String[]> allProjectPrivileges = new HashMap<Integer, String[]>();
	ArrayList<Object[]> projects = new ArrayList<Object[]>();

	public void authenticate() {

		try {
			// SQL Statement for fetching user info
			PreparedStatement st = (PreparedStatement) this.conn
					.prepareStatement("Select id,user_name, password from Users where user_name=? and password=?");
			st.setString(1, username);
			st.setString(2, password);

			// Execute SQL Statement
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				dispose(); // Disposing Login Page JFrame
				userId = rs.getInt("id"); // Get user id from result set
				chooseProject();
			} else {
				JOptionPane.showMessageDialog(null, "Wrong Username & Password");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void chooseProject() {

		// Creating Dialog Box
		JDialog d = new JDialog();
		d.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		// Get Data related to projects and privileges
		getData();

		gbc.gridx = 1;
		gbc.gridy = 1;

		JComboBox comboBox = new JComboBox();
		// Adding projects to combo box 
		for (Object[] project : projects) {
			comboBox.addItem(project[1]);
		}
		d.add(comboBox, gbc);

		gbc.gridx = 1;
		gbc.gridy = 2;
		JButton b = new JButton("Continue");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get Selected Project and Open Main Page
				int index = comboBox.getSelectedIndex();
				Object[] project = projects.get(index);
				privileges = allProjectPrivileges.get(project[0]);
				currentPid = (int) project[0];
				d.dispose();
				MainPage main = new MainPage(conn, userId, privileges, projects, currentPid);
			}
		});
		d.add(b, gbc);
		d.setVisible(true);
		d.setSize(300, 200);
	}

	public LoginPage(Connection conn) {
		// Overriding Defaults of Java Swing
		setUIFont(new javax.swing.plaf.FontUIResource("Arial", Font.PLAIN, 14));

		setLayout(new GridBagLayout());
		Color backgroundColor = new Color(242, 243, 244);
		setBackground(backgroundColor);
		Color submitBtnColor = new Color(52, 132, 240);
		this.conn = conn;

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 1;
		gbc.gridy = 0;
		add(new JLabel("Username"), gbc);
		gbc.gridx = 1;
		gbc.gridy = 2;
		add(new JLabel("Password"), gbc);

		gbc.insets = new Insets(0, 0, 10, 0);
		gbc.gridx = 1;
		gbc.gridy = 1;
		JTextField usernameField = new JTextField();
		usernameField.setPreferredSize(new Dimension(200, 25));
		add(usernameField, gbc);

		gbc.gridy = 3;
		JPasswordField passwordField = new JPasswordField();
		passwordField.setPreferredSize(new Dimension(200, 25));
		add(passwordField, gbc);

		gbc.gridy = 4;

		gbc.gridy = 7;
		JButton submitBtn = new JButton("Submit");

		// When submit button is clicked
		submitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// Get username and password from text fields
				password = new String(passwordField.getPassword());
				username = usernameField.getText();

				// Check if text fields are not empty
				if (username.equals("") || username.equals(" ")) {
					JOptionPane.showMessageDialog(null, "Username Cannot be left blank", "",
							JOptionPane.WARNING_MESSAGE);

				} else if (password.equals("") || password.equals(" ")) {
					JOptionPane.showMessageDialog(null, "Password Cannot be left blank", "",
							JOptionPane.WARNING_MESSAGE);
					return;
				} else {
					// Autheticate User
					authenticate();
				}
			}
		});
		submitBtn.setFocusPainted(false);
		submitBtn.setForeground(Color.WHITE);
		submitBtn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		submitBtn.setBackground(submitBtnColor);
		add(submitBtn, gbc);

		setSize(300, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		setTitle("Login");
	}

	// Change UI defaults
	public static void setUIFont(javax.swing.plaf.FontUIResource f) {
		Enumeration<Object> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, f);
		}
	}

	public void getData() {
		try {
			// Get privileges associated with the user authenticated
			PreparedStatement statement = conn.prepareStatement("SELECT pid,type FROM Privileges WHERE uid = ?");
			statement.setInt(1, userId);

			// Execute Query
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				String type = rs.getString("type");
				int pid = rs.getInt("pid");
				Boolean exists = false;

				// Check if Project exists in allProjectPrivileges
				for (int i : allProjectPrivileges.keySet()) {
					if (i == pid) {
						exists = true;
						break;
					}
				}

				if (!exists) {
					String[] temparr = type.equals("admin") ? (new String[] { "admin", "", "" })
							: (type.equals("developer") ? new String[] { "", "developer", "" }
									: new String[] { "", "", "tester" });

					// Add to allProjectPrivileges
					allProjectPrivileges.put(pid, temparr);

				} else {
					// If project exist in allProjectPrivileges
					String[] temparr = new String[3];
					temparr = allProjectPrivileges.get(pid);
					if (type.equals("admin")) {
						temparr[0] = "admin";
					} else if (type.equals("developer")) {
						temparr[1] = "developer";
					} else {
						temparr[2] = "tester";
					}
				}
			}

			// Setting The First Projects Privileges by default
			if (!allProjectPrivileges.isEmpty()) {
				Object[] keys = allProjectPrivileges.keySet().toArray();
				this.privileges = allProjectPrivileges.get((Integer) keys[0]);
			}

			// Finding all Project Names
			for (Integer i : allProjectPrivileges.keySet()) {
				statement = conn.prepareStatement("SELECT id, project_name FROM Projects WHERE id=?");
				statement.setInt(1, i);
				rs = statement.executeQuery();
				while (rs.next()) {
					int pid = rs.getInt("id");
					String name = rs.getString("project_name");
					projects.add(new Object[] { pid, name });
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}