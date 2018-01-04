package fr.pavnay.rabbits.ui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JComponent;

/**
 * 
 * An utility class for UI
 *
 */
public class UiUtils {

	/**
	 * Add a component in a container with a GridBagLayout
	 * 
	 * @param container
	 * @param component
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public static void add(Container container, JComponent component, int x, int y,
			int w, int h) {
		GridBagConstraints bagConstraints = new GridBagConstraints();
		bagConstraints.gridx = x;
		bagConstraints.gridy = y;
		bagConstraints.gridwidth = w;
		bagConstraints.gridheight = h;
		bagConstraints.anchor = (x == 0) ? GridBagConstraints.EAST
				: GridBagConstraints.WEST;
		bagConstraints.fill = (x == 0) ? GridBagConstraints.BOTH
				: GridBagConstraints.HORIZONTAL;
		bagConstraints.insets = new Insets(5, 5, 5, 5);
		container.add(component, bagConstraints);
	}
}
