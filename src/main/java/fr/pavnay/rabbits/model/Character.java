package fr.pavnay.rabbits.model;

import java.awt.Point;

/**
 * 
 * An interface to manage hunter and rabbits in the same way.
 *
 */
public interface Character {

	public Point getLocation();
	public Point getDestination();
	public void setDestination(Point destination);
	
	public int getSpeed();
	public void increaseDistance(int distance);
}
