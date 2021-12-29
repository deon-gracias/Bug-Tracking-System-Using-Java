package components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SideNavBar extends JPanel {
	public String[] priviliges;
	ArrayList<Object[]> comboBoxItems = new ArrayList<Object[]>();

	Color labelBackgroundColors = new Color(196, 219, 250);
	Color labelForegroundColors = Color.BLACK;
	public JButton reportBugButton, testBugsReportedButton, devBugsReportedButton, manageMembersButton,
			devReportBugButton;

	public SideNavBar(String[] priviligesArray, ArrayList<Object[]> projects) {
		this.priviliges = priviligesArray;
		this.comboBoxItems = projects;
		Color backgroundColor = new Color(211, 211, 211);
		setBackground(backgroundColor);

		List<JComponent> components = renderSideBar();
		for (JComponent component : components) {
			add(component);
		}

		setLayout(new GridLayout(15, 1));
		setPreferredSize(new Dimension(200, 600));
	}

	private List<JComponent> renderSideBar() {
		List<JComponent> components = new ArrayList<JComponent>();

		for (String privilige : priviliges) {
			if (privilige.equals("admin")) {
				JLabel jlabel = new JLabel("Admin", JLabel.CENTER);
				jlabel.setOpaque(true);
				jlabel.setBackground(labelBackgroundColors);
				jlabel.setForeground(labelForegroundColors);
				components.add(jlabel);

//				Report Bug Button
				manageMembersButton = new JButton("Manage Members");
				manageMembersButton.setFocusPainted(false);
				manageMembersButton.setForeground(Color.BLACK);
				manageMembersButton.setBackground(Color.WHITE);
				manageMembersButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				manageMembersButton.addMouseListener(new HighLightEffect());
				components.add(manageMembersButton);

				continue;
			}
			if (privilige.equals("developer")) {
				JLabel jlabel = new JLabel("Developer", JLabel.CENTER);
				jlabel.setOpaque(true);
				jlabel.setBackground(labelBackgroundColors);
				jlabel.setForeground(labelForegroundColors);
				components.add(jlabel);

//				Report Bug Button
				devReportBugButton = new JButton("Report Bug");
				devReportBugButton.setFocusPainted(false);
				devReportBugButton.setForeground(Color.BLACK);
				devReportBugButton.setBackground(Color.WHITE);
				devReportBugButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				devReportBugButton.addMouseListener(new HighLightEffect());
				components.add(devReportBugButton);

//				Developer Bugs Reported Button
				devBugsReportedButton = new JButton("Bugs Reported");
				devBugsReportedButton.setFocusPainted(false);
				devBugsReportedButton.setForeground(Color.BLACK);
				devBugsReportedButton.setBackground(Color.WHITE);
				devBugsReportedButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				devBugsReportedButton.addMouseListener(new HighLightEffect());
				components.add(devBugsReportedButton);
				continue;
			}
			if (privilige.equals("tester")) {

				JLabel jlabel = new JLabel("Tester", JLabel.CENTER);
				jlabel.setOpaque(true);
				jlabel.setBackground(labelBackgroundColors);
				jlabel.setForeground(labelForegroundColors);
				components.add(jlabel);

//				Report Bug Button
				reportBugButton = new JButton("Report Bug");
				reportBugButton.setFocusPainted(false);
				reportBugButton.setForeground(Color.BLACK);
				reportBugButton.setBackground(Color.WHITE);
				reportBugButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				reportBugButton.addMouseListener(new HighLightEffect());
//				reportBugButton.setOpaque(false);
				components.add(reportBugButton);

				continue;
			}
		}
		return components;
	}

}
