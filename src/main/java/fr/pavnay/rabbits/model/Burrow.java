package fr.pavnay.rabbits.model;

import java.awt.Point;

/**
 * 
 * A rabbit burrow - Only one rabbit per burrow.
 * Burrows are filled when a rabbit is purchased.
 *
 */
public class Burrow {
    
    private Point location;
    private boolean free;
    
    public Burrow( Point location ) {
        this.location = location;
        free = true;
    }
    
    public Point getLocation() {
        return location;
    }
    public void setFree(boolean free) {
		this.free = free;
	}
    public boolean isFree() {
        return free;
    }

	@Override
	public String toString() {
		return "Burrow [location=" + location + ", free=" + free + "]";
	}
    
}
