package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import components.BugsReported;
import components.ManageMembers;
import components.ReportBugForm;
import components.SideNavBar;

public class MainPage extends JFrame {
	int userId;
	Connection conn;
	String[] privileges = { "", "", "" };
	HashMap<Integer, String[]> allProjectPrivileges = new HashMap<Integer, String[]>();
	ArrayList<Object[]> projects = new ArrayList<Object[]>();

	public void getData() {
		try {
			PreparedStatement statement = conn.prepareStatement("SELECT pid,type FROM Privileges WHERE uid = ?");

			statement.setInt(1, userId);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				String type = rs.getString("type");
				int pid = rs.getInt("pid");
				Boolean exists = false;
				for (int i : allProjectPrivileges.keySet()) {
					if (i == pid) {
						exists = true;
						break;
					}
				}

//				Checking if project already exists in Dictionary
				if (!exists) {
					String[] temparr = type.equals("admin") ? (new String[] { "admin", "", "" })
							: (type.equals("developer") ? new String[] { "", "developer", "" }
									: new String[] { "", "", "tester" });
					allProjectPrivileges.put(pid, temparr);

				} else {
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

//			Setting The First Porjects Privileges by default
			if (!allProjectPrivileges.isEmpty()) {
				Object[] keys = allProjectPrivileges.keySet().toArray();
				this.privileges = allProjectPrivileges.get((Integer) keys[0]);
			}

//			Finding all Project Names
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

		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}

	public MainPage(Connection conn, int userId) {
		this.conn = conn;
		this.userId = userId;
		setUIFont(new javax.swing.plaf.FontUIResource("Arial", Font.PLAIN, 14));
		getData();
		Container container;
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		container = getContentPane();
		Color backgroundColor = new Color(247, 247, 247);
		container.setBackground(backgroundColor);

		SideNavBar sideNavBar = new SideNavBar(privileges, projects);
		ReportBugForm reportBugForm = new ReportBugForm();
		ManageMembers manageMembers = new ManageMembers();
		BugsReported bugsReported = new BugsReported();

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
