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
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class LoginPage extends JFrame {
	String username;
	String password;

	public void authenticate(Connection conn) {

		try {
			PreparedStatement st = (PreparedStatement) conn
					.prepareStatement("Select id,user_name, password from Users where user_name=? and password=?");

			st.setString(1, username);
			st.setString(2, password);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				dispose();
				MainPage main = new MainPage(conn, rs.getInt("id"));

				JOptionPane.showMessageDialog(null, "You have successfully logged in");
			} else {
				JOptionPane.showMessageDialog(null, "Wrong Username & Password");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public LoginPage(Connection conn) {
		setUIFont(new javax.swing.plaf.FontUIResource("Arial", Font.PLAIN, 14));
		setLayout(new GridBagLayout());
		Color backgroundColor = new Color(242, 243, 244);
		setBackground(backgroundColor);
		Color submitBtnColor = new Color(52, 132, 240);

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
		JButton submitBtn = new JButton("Submit");
		submitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				password = new String(passwordField.getPassword());
				username = usernameField.getText();
				if (username.equals("") || username.equals(" ")) {
					JOptionPane.showMessageDialog(null, "Username Cannot be left blank", "",
							JOptionPane.WARNING_MESSAGE);

				} else if (password.equals("") || password.equals(" ")) {
					JOptionPane.showMessageDialog(null, "Password Cannot be left blank", "",
							JOptionPane.WARNING_MESSAGE);
					return;
				} else {
					authenticate(conn);
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

	public static void setUIFont(javax.swing.plaf.FontUIResource f) {
		Enumeration<Object> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, f);
		}
	}
}