package fr.pavnay.rabbits.ui.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import fr.pavnay.rabbits.model.Burrow;
import fr.pavnay.rabbits.model.Forest;
import fr.pavnay.rabbits.model.Hunter;
import fr.pavnay.rabbits.model.Rabbit;
import fr.pavnay.rabbits.model.Tree;

/**
 * 
 * This class draws all hunt elements : trees, burrows, hunter and rabbits
 *
 */
public class Canvas extends JPanel {

	private static final long serialVersionUID = 4209640358977009595L;

	protected static int RATE = 5;
	protected static int ITEM_RATIO = 2;

	private Forest forest;
	private Hunter hunter;
	
	public Canvas( Forest forest, Hunter hunter) {
		this.hunter = hunter;
		this.forest = forest;
	}	
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		
		graphics.setColor(Color.RED);
		graphics.fillOval((int) hunter.getLocation().getX() * RATE, (int) hunter.getLocation().getY() * RATE, 3 * ITEM_RATIO, 3 * ITEM_RATIO);
		
		graphics.setColor(Color.GREEN);
		for( Tree tree : forest.getTrees() ) {
			graphics.fillOval((int) tree.getLocation().getX() * RATE, (int) tree.getLocation().getY() * RATE, 5 * ITEM_RATIO, 5 * ITEM_RATIO);
		}
		
		for( Burrow burrow : forest.getBurrows() ) {
			if( burrow.isFree() ) {
				graphics.setColor(Color.GRAY);
			} else {
				graphics.setColor(Color.DARK_GRAY);
			}
			graphics.fillOval((int) burrow.getLocation().getX() * RATE, (int) burrow.getLocation().getY() * RATE, 4 * ITEM_RATIO, 2 * ITEM_RATIO);
		}
		
		for( Rabbit rabbit : forest.getRabbits() ) {
			if( rabbit.getColor() == fr.pavnay.rabbits.model.enums.Color.WHITE ) {
				graphics.setColor(Color.BLACK);
				graphics.drawOval((int) rabbit.getLocation().getX() * RATE, (int) rabbit.getLocation().getY() * RATE, 3 * ITEM_RATIO, 3 * ITEM_RATIO);
			} else {
				graphics.setColor(rabbit.getColor().getColor());
				graphics.fillOval((int) rabbit.getLocation().getX() * RATE, (int) rabbit.getLocation().getY() * RATE, 3 * ITEM_RATIO, 3 * ITEM_RATIO);
			}
		}
    }
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(forest.getEdgeArea() * RATE, forest.getEdgeArea() * RATE);
	}

}
