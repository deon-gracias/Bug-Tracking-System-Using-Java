package components;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

public class HighLightEffect extends MouseAdapter {
	Color prevColor;

	HighLightEffect() {

	}

	public void mouseEntered(MouseEvent evt) {
		JButton btn = (JButton) evt.getSource();
		prevColor = btn.getBackground();
		btn.setBackground(new Color(52, 132, 240));
	}

	public void mouseExited(java.awt.event.MouseEvent evt) {
		JButton btn = (JButton) evt.getSource();
		btn.setBackground(prevColor);
	}
}
