package components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ReportBugForm extends JPanel {
	String title, priority, desc;
	JTextField titleField, priorityField;
	JTextArea descField;
	JButton submitBtn;

	public ReportBugForm() {

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

		setBorder(new EmptyBorder(10, 10, 10, 10));
		setPreferredSize(new Dimension(585, 600));
	}
}
