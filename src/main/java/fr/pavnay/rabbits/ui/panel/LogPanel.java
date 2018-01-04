package fr.pavnay.rabbits.ui.panel;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 * This class provides some hunt events
 *
 */
public class LogPanel extends JPanel {

	private static final long serialVersionUID = 3786097551382633033L;

	public void addLog(String log) {
		JLabel label = new JLabel(log);
		label.setFont(label.getFont().deriveFont(Font.PLAIN));
		add(label);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(200, 400);
	}
}
