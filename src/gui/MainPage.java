package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import components.BugsReported;
import components.ManageMembers;
import components.ReportBugForm;
import components.SideNavBar;

public class MainPage extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3826054681629138296L;
	int userId;
	Connection conn;
	String[] privileges = { "", "", "" };
	ArrayList<Object[]> projects = new ArrayList<Object[]>();

	public MainPage(Connection conn, int userId, String[] privileges, ArrayList<Object[]> projects, int projectId) {
		this.conn = conn;
		this.userId = userId;
		this.privileges = privileges;
		this.projects = projects;

		setUIFont(new javax.swing.plaf.FontUIResource("Arial", Font.PLAIN, 14));
		Container container;
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		container = getContentPane();
		Color backgroundColor = new Color(247, 247, 247);
		container.setBackground(backgroundColor);

		// Custom Components
		SideNavBar sideNavBar = new SideNavBar(privileges, projects);
		ReportBugForm reportBugForm = new ReportBugForm(conn, userId, projectId);
		ManageMembers manageMembers = new ManageMembers(conn, userId, projectId);
		BugsReported bugsReported = new BugsReported(conn, userId, projectId);

		JPanel cardPanel = new JPanel();
		CardLayout cl = new CardLayout();

		cardPanel.setLayout(cl);

		cardPanel.add(reportBugForm, "Report Bug");
		cardPanel.add(manageMembers, "Manage Members");
		cardPanel.add(bugsReported, "Bugs Reported");

		// Manage Members
		if (sideNavBar.manageMembersButton != null) {
			sideNavBar.manageMembersButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					cl.show(cardPanel, "Manage Members");
				}
			});
		}
		// Developer Report Bug Button
		if (sideNavBar.devReportBugButton != null) {
			sideNavBar.devReportBugButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent ae) {
					cl.show(cardPanel, "Report Bug");
				}
			});
		}
		// Developer Bugs Reported Button
		if (sideNavBar.devBugsReportedButton != null) {
			sideNavBar.devBugsReportedButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					cl.show(cardPanel, "Bugs Reported");
				}
			});
		}
		// Tester Report Bug Button
		if (sideNavBar.reportBugButton != null)

		{
			sideNavBar.reportBugButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					cl.show(cardPanel, "Report Bug");
				}
			});
		}
		// Tester Bugs Reported Button
		if (sideNavBar.testBugsReportedButton != null) {
			sideNavBar.testBugsReportedButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					cl.show(cardPanel, "Bugs Reported");
				}
			});

		}
		container.add(cardPanel, BorderLayout.CENTER);
		container.add(sideNavBar, BorderLayout.WEST);

		setVisible(true);

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
