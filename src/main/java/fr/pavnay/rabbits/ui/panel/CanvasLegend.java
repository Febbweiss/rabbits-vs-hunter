package fr.pavnay.rabbits.ui.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import fr.pavnay.rabbits.model.Forest;

/**
 * 
 * This class draws the Canvas elements legend
 *
 */
public class CanvasLegend extends JPanel {

	private static final long serialVersionUID = 1307357862666434838L;

	private int width;
	
	public CanvasLegend(Forest forest) {
		this.width = forest.getEdgeArea() * Canvas.RATE;
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		
		graphics.setColor(Color.RED);
		graphics.fillOval(5, 10, 3 * Canvas.ITEM_RATIO, 3 * Canvas.ITEM_RATIO);
		graphics.drawString("Hunter", 12, 15);
		
		graphics.setColor(Color.GREEN);
		graphics.fillOval(width / 5 + 5, 10, 5 * Canvas.ITEM_RATIO, 5 * Canvas.ITEM_RATIO);
		graphics.drawString("Trees", width / 5 + 12, 15);
		
		graphics.setColor(Color.GRAY);
		graphics.fillOval(2 * width / 5 + 5, 10, 4 * Canvas.ITEM_RATIO, 2 * Canvas.ITEM_RATIO);
		graphics.drawString("Burrows", 2 * width / 5 + 12, 15);
		
		graphics.setColor(Color.BLACK);
		graphics.drawOval(3 * width / 5 + 5, 10, 3 * Canvas.ITEM_RATIO, 3 * Canvas.ITEM_RATIO);
		graphics.drawString("Rabbits", 3 * width / 5 + 12, 15);
		
		graphics.setColor(fr.pavnay.rabbits.model.enums.Color.BROWN.getColor());
		graphics.fillOval(4 * width / 5 + 5, 10, 3 * Canvas.ITEM_RATIO, 3 * Canvas.ITEM_RATIO);
		graphics.drawString("Rabbits", 4 * width / 5 + 12, 15);
    }
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, 20);
	}
}
