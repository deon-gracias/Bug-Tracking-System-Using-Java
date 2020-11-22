package components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ReportBugForm extends JPanel {
	String title, priority, desc;
	int userId, projectId;
	JTextField titleField, priorityField;
	JTextArea descField;
	JButton submitBtn;
	Connection conn;

	public ReportBugForm(Connection conn, int userId, int projectId) {
		this.conn = conn;

		Color backgroundColor = new Color(242, 243, 244);
		setBackground(backgroundColor);
		Color submitBtnColor = new Color(52, 132, 240);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		setLayout(new GridBagLayout());
		titleField = new JTextField(50);
		priorityField = new JTextField(50);
		descField = new JTextArea(10, 50);

		submitBtn = new JButton("Submit");
		submitBtn.setFocusPainted(false);
		submitBtn.setForeground(Color.WHITE);
		submitBtn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		submitBtn.setBackground(submitBtnColor);

		gbc.insets = new Insets(0, 0, 5, 0);
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(new JLabel("Title"), gbc);
		gbc.gridx = 0;
		gbc.gridy = 3;
		add(new JLabel("Priority"), gbc);
		gbc.gridx = 0;
		gbc.gridy = 5;
		add(new JLabel("Description"), gbc);

		gbc.insets = new Insets(0, 0, 10, 0);
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(titleField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		add(priorityField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 6;
		add(descField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 7;
		add(submitBtn, gbc);

		submitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String title = titleField.getText();
				String priority = priorityField.getText();
				String description = descField.getText();

				if (title.equals("") || title.equals(" ")) {
					JOptionPane.showMessageDialog(null, "Title Field is empty.");
					return;
				} else if (priority.equals("") || priority.equals(" ")) {
					JOptionPane.showMessageDialog(null, "Priority Field is empty.");
					return;
				} else if (description.equals("") || description.equals(" ")) {
					JOptionPane.showMessageDialog(null, "Description Field is empty.");
					return;
				} else {
					submitBugReportForm(title, priority, description, userId, projectId);
					titleField.setText("");
					priorityField.setText("");
					descField.setText("");
				}

			}
		});

		setBorder(new EmptyBorder(10, 10, 10, 10));
		setPreferredSize(new Dimension(585, 600));
	}

	void submitBugReportForm(String title, String priority, String description, int userId, int projectId) {
		try {
			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO Bugs(title,priority,description,status,reportedBy,pid) VALUES(?,?,?,?,?,?);");
			ps.setString(1, title);
			ps.setString(2, priority);
			ps.setString(3, description);
			ps.setString(4, "Pending");
			ps.setInt(5, userId);
			ps.setInt(6, projectId);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
